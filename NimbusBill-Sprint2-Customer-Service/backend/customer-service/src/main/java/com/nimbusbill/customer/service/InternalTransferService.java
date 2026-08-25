package com.nimbusbill.customer.service;

import com.nimbusbill.customer.dto.*;
import com.nimbusbill.customer.entity.*;
import com.nimbusbill.customer.exception.*;
import com.nimbusbill.customer.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Service
@Transactional
public class InternalTransferService {
    private static final String EVENT = "INTERNAL_TRANSFER_COMPLETED";
    private final TransferAccountRepository accounts;
    private final TransferTransactionRepository transfers;
    private final FundReservationRepository reservations;
    private final LedgerTransactionRepository ledgers;
    private final BillableEventRepository events;
    private final CustomerRepository customers;
    private final PricingPlanVersionRepository versions;
    private final PricingService pricing;

    public InternalTransferService(TransferAccountRepository accounts, TransferTransactionRepository transfers,
            FundReservationRepository reservations, LedgerTransactionRepository ledgers,
            BillableEventRepository events, CustomerRepository customers,
            PricingPlanVersionRepository versions, PricingService pricing) {
        this.accounts=accounts; this.transfers=transfers; this.reservations=reservations; this.ledgers=ledgers;
        this.events=events; this.customers=customers; this.versions=versions; this.pricing=pricing;
    }

    public TransferAccountResponse createAccount(TransferAccountRequest r) {
        Customer customer=r.customerId()==null?null:customers.findById(r.customerId())
                .orElseThrow(()->new ResourceNotFoundException("Customer not found: "+r.customerId()));
        if(r.accountType()==TransferAccount.AccountType.CLIENT&&customer==null)
            throw new IllegalArgumentException("customerId is required for CLIENT accounts");
        TransferAccount a=new TransferAccount(); a.setCustomer(customer); a.setAccountNumber(r.accountNumber().trim());
        a.setAccountName(r.accountName().trim()); a.setAccountType(r.accountType()); a.setCurrency(r.currency().toUpperCase());
        a.setLedgerBalance(r.openingBalance()); a.setAvailableBalance(r.openingBalance());
        return account(accounts.save(a));
    }

    @Transactional(readOnly=true)
    public TransferAccountResponse getAccount(UUID id) {
        return account(accounts.findById(id).orElseThrow(()->new ResourceNotFoundException("Transfer account not found: "+id)));
    }

    @Transactional(readOnly=true)
    public List<TransferAccountResponse> listAccounts(UUID customerId) {
        customers.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer not found: "+customerId));
        return accounts.findByCustomerIdOrCustomerIsNullOrderByAccountName(customerId).stream().map(this::account).toList();
    }

    public InternalTransferResponse initiate(InternalTransferRequest r) {
        String clientReferenceId=r.clientReferenceId().trim();
        Optional<TransferTransaction> duplicate=transfers.findByCustomerIdAndClientReferenceId(r.customerId(),clientReferenceId);
        if(duplicate.isPresent()) return response(duplicate.get());
        Customer customer=customers.findById(r.customerId()).orElseThrow(()->new ResourceNotFoundException("Customer not found: "+r.customerId()));
        Map<UUID,TransferAccount> locked=lockAll(List.of(r.sourceAccountId(),r.sourceBridgeAccountId(),r.destinationBridgeAccountId(),r.destinationAccountId()));
        duplicate=transfers.findByCustomerIdAndClientReferenceId(r.customerId(),clientReferenceId);
        if(duplicate.isPresent()) return response(duplicate.get());
        TransferAccount source=locked.get(r.sourceAccountId()), xa=locked.get(r.sourceBridgeAccountId());
        TransferAccount ya=locked.get(r.destinationBridgeAccountId()), destination=locked.get(r.destinationAccountId());
        validate(r,customer,source,xa,ya,destination);
        PricingPreviewResponse charge=pricing.priceCustomer(customer.getId(),r.productCode(),r.amount(),LocalDate.now());
        if(!charge.currency().equalsIgnoreCase(r.currency())) throw new ConflictException("Pricing currency "+charge.currency()+" does not match transfer currency "+r.currency());
        PricingPlanVersion priceVersion=versions.findById(charge.versionId()).orElseThrow();

        TransferTransaction tx=new TransferTransaction(); tx.setClientReferenceId(clientReferenceId); tx.setCustomer(customer);
        tx.setSourceAccount(source); tx.setSourceBridgeAccount(xa); tx.setDestinationBridgeAccount(ya); tx.setDestinationAccount(destination);
        tx.setProductCode(r.productCode().trim().toUpperCase()); tx.setAmount(r.amount()); tx.setCurrency(r.currency().toUpperCase());
        tx.setPricingPlanVersion(priceVersion); transfers.save(tx);

        if(source.getAvailableBalance().compareTo(r.amount())<0) throw new ConflictException("Insufficient available funds");
        source.setAvailableBalance(source.getAvailableBalance().subtract(r.amount()));
        tx.setStatus(TransferTransaction.TransferStatus.FUNDS_CHECKED);
        FundReservation reservation=new FundReservation(); reservation.setTransfer(tx); reservation.setAccount(source);
        reservation.setAmount(r.amount()); reservation.setStatus(FundReservation.ReservationStatus.ACTIVE);
        reservation.setExpiresAt(Instant.now().plusSeconds(300)); reservations.save(reservation);

        post(tx,(short)1,"SOURCE_TO_XA",source,xa,true);
        post(tx,(short)2,"XA_TO_YA",xa,ya,false);
        post(tx,(short)3,"YA_TO_DESTINATION",ya,destination,false);
        reservation.setStatus(FundReservation.ReservationStatus.CONSUMED);
        tx.setStatus(TransferTransaction.TransferStatus.COMPLETED); tx.setCompletedAt(Instant.now());
        createBillableEvent(tx,customer,priceVersion,charge); tx.setBillingStatus(TransferTransaction.BillingStatus.BILLED);
        return response(tx);
    }

    @Transactional(readOnly=true)
    public InternalTransferResponse get(UUID id) { return response(transfers.findById(id).orElseThrow(()->new ResourceNotFoundException("Transfer not found: "+id))); }
    @Transactional(readOnly=true)
    public List<InternalTransferResponse> list(UUID customerId) { return transfers.findByCustomerIdOrderByCreatedAtDesc(customerId).stream().map(this::response).toList(); }

    private Map<UUID,TransferAccount> lockAll(List<UUID> ids) {
        if(new HashSet<>(ids).size()!=4) throw new IllegalArgumentException("All four transfer accounts must be different");
        Map<UUID,TransferAccount> result=new HashMap<>();
        ids.stream().sorted().forEach(id->result.put(id,accounts.lockById(id).orElseThrow(()->new ResourceNotFoundException("Transfer account not found: "+id))));
        return result;
    }

    private void validate(InternalTransferRequest r,Customer customer,TransferAccount... a) {
        String currency=r.currency().toUpperCase();
        for(TransferAccount account:a){
            if(account.getStatus()!=TransferAccount.AccountStatus.ACTIVE) throw new ConflictException("Transfer account is not active: "+account.getId());
            if(!account.getCurrency().equalsIgnoreCase(currency)) throw new ConflictException("All accounts must use transfer currency "+currency);
        }
        if(a[0].getCustomer()==null||!a[0].getCustomer().getId().equals(customer.getId())) throw new ConflictException("Source account does not belong to customer");
        if(a[0].getAccountType()!=TransferAccount.AccountType.CLIENT||a[3].getAccountType()!=TransferAccount.AccountType.CLIENT) throw new IllegalArgumentException("Source and destination must be CLIENT accounts");
        if(a[1].getAccountType()!=TransferAccount.AccountType.INTERNAL||a[2].getAccountType()!=TransferAccount.AccountType.INTERNAL) throw new IllegalArgumentException("Bridge accounts must be INTERNAL accounts");
    }

    private void post(TransferTransaction tx,short sequence,String type,TransferAccount debit,TransferAccount credit,boolean sourceReserved) {
        if(debit.getLedgerBalance().compareTo(tx.getAmount())<0) throw new ConflictException("Insufficient ledger funds for leg "+sequence);
        if(!sourceReserved&&debit.getAvailableBalance().compareTo(tx.getAmount())<0) throw new ConflictException("Insufficient available funds for leg "+sequence);
        Instant now=Instant.now(); debit.setLedgerBalance(debit.getLedgerBalance().subtract(tx.getAmount()));
        if(!sourceReserved) debit.setAvailableBalance(debit.getAvailableBalance().subtract(tx.getAmount()));
        credit.setLedgerBalance(credit.getLedgerBalance().add(tx.getAmount())); credit.setAvailableBalance(credit.getAvailableBalance().add(tx.getAmount()));
        TransferLeg leg=new TransferLeg(); leg.setTransfer(tx); leg.setLegSequence(sequence); leg.setLegType(type);
        leg.setDebitAccount(debit); leg.setCreditAccount(credit); leg.setAmount(tx.getAmount()); leg.setCurrency(tx.getCurrency());
        leg.setStatus(TransferLeg.LegStatus.POSTED); leg.setPostedAt(now); leg.setLedgerReference(tx.getId()+"-"+sequence);
        tx.getLegs().add(leg); transfers.saveAndFlush(tx);
        LedgerTransaction ledger=new LedgerTransaction(); ledger.setTransfer(tx); ledger.setLeg(leg); ledger.setTransactionType(type); ledger.setStatus("POSTED"); ledger.setPostedAt(now);
        ledger.getEntries().add(entry(ledger,debit,LedgerEntry.EntryType.DEBIT,tx)); ledger.getEntries().add(entry(ledger,credit,LedgerEntry.EntryType.CREDIT,tx)); ledgers.save(ledger);
        tx.setStatus(TransferTransaction.TransferStatus.IN_PROGRESS); tx.setCurrentStep(sequence);
    }

    private LedgerEntry entry(LedgerTransaction ledger,TransferAccount account,LedgerEntry.EntryType type,TransferTransaction tx) {
        LedgerEntry e=new LedgerEntry(); e.setLedgerTransaction(ledger); e.setAccount(account); e.setEntryType(type); e.setAmount(tx.getAmount()); e.setCurrency(tx.getCurrency()); return e;
    }
    private void createBillableEvent(TransferTransaction tx,Customer customer,PricingPlanVersion version,PricingPreviewResponse p) {
        if(events.existsByTransferIdAndEventType(tx.getId(),EVENT)) return;
        BillableEvent e=new BillableEvent(); e.setTransfer(tx); e.setCustomer(customer); e.setEventType(EVENT); e.setPricingPlanVersion(version);
        e.setBaseFee(p.baseFee()); e.setTaxAmount(p.taxAmount()); e.setChargeAmount(p.totalCharge()); e.setCurrency(p.currency()); events.save(e);
    }
    private TransferAccountResponse account(TransferAccount a) { return new TransferAccountResponse(a.getId(),a.getCustomer()==null?null:a.getCustomer().getId(),a.getAccountNumber(),a.getAccountName(),a.getAccountType(),a.getCurrency(),a.getLedgerBalance(),a.getAvailableBalance(),a.getStatus()); }
    private InternalTransferResponse response(TransferTransaction t) {
        BigDecimal charge=events.findByTransferIdAndEventType(t.getId(),EVENT).map(BillableEvent::getChargeAmount).orElse(null);
        return new InternalTransferResponse(t.getId(),t.getClientReferenceId(),t.getCustomer().getId(),t.getAmount(),t.getCurrency(),t.getStatus(),t.getCurrentStep(),t.getBillingStatus(),t.getPricingPlanVersion()==null?null:t.getPricingPlanVersion().getId(),charge,t.getCreatedAt(),t.getCompletedAt(),t.getLegs().stream().map(l->new InternalTransferResponse.Leg(l.getId(),l.getLegSequence(),l.getLegType(),l.getDebitAccount().getId(),l.getCreditAccount().getId(),l.getStatus(),l.getLedgerReference(),l.getPostedAt())).toList());
    }
}
