package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="transfer_accounts") public class TransferAccount{
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @ManyToOne @JoinColumn(name="customer_id") private Customer customer;
 @Column(name="account_number",nullable=false,unique=true,length=80) private String accountNumber; @Column(name="account_name",nullable=false,length=150) private String accountName;
 @Enumerated(EnumType.STRING) @Column(name="account_type",nullable=false,length=20) private AccountType accountType; @Column(nullable=false,length=3) private String currency;
 @Column(name="ledger_balance",nullable=false,precision=19,scale=4) private BigDecimal ledgerBalance; @Column(name="available_balance",nullable=false,precision=19,scale=4) private BigDecimal availableBalance;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private AccountStatus status; @Version private long version;
 @Column(name="created_at",nullable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 @PrePersist void create(){Instant n=Instant.now();createdAt=n;updatedAt=n;if(status==null)status=AccountStatus.ACTIVE;if(ledgerBalance==null)ledgerBalance=BigDecimal.ZERO;if(availableBalance==null)availableBalance=ledgerBalance;} @PreUpdate void update(){updatedAt=Instant.now();}
 public UUID getId(){return id;} public Customer getCustomer(){return customer;} public void setCustomer(Customer v){customer=v;} public String getAccountNumber(){return accountNumber;} public void setAccountNumber(String v){accountNumber=v;} public String getAccountName(){return accountName;} public void setAccountName(String v){accountName=v;} public AccountType getAccountType(){return accountType;} public void setAccountType(AccountType v){accountType=v;} public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;} public BigDecimal getLedgerBalance(){return ledgerBalance;} public void setLedgerBalance(BigDecimal v){ledgerBalance=v;} public BigDecimal getAvailableBalance(){return availableBalance;} public void setAvailableBalance(BigDecimal v){availableBalance=v;} public AccountStatus getStatus(){return status;} public void setStatus(AccountStatus v){status=v;}
 public enum AccountType{CLIENT,INTERNAL} public enum AccountStatus{ACTIVE,BLOCKED,CLOSED}
}
