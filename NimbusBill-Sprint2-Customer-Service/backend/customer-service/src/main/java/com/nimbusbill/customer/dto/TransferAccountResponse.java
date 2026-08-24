package com.nimbusbill.customer.dto; import com.nimbusbill.customer.entity.TransferAccount; import java.math.BigDecimal; import java.util.UUID;
public record TransferAccountResponse(UUID id,UUID customerId,String accountNumber,String accountName,TransferAccount.AccountType accountType,String currency,BigDecimal ledgerBalance,BigDecimal availableBalance,TransferAccount.AccountStatus status){}
