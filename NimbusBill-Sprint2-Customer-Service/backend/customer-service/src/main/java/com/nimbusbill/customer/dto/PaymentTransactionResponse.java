package com.nimbusbill.customer.dto;
import com.nimbusbill.customer.entity.PaymentTransactionStatus; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
public record PaymentTransactionResponse(UUID id,UUID customerId,String clientReferenceId,String productCode,String transactionType,
 BigDecimal amount,String currency,String sourceAccount,String destinationAccount,PaymentTransactionStatus status,String failureReason,
 BigDecimal baseFee,BigDecimal taxAmount,BigDecimal totalCharge,UUID pricingPlanVersionId,String chargeStatus,String transactionKind,String ingestionSource,UUID originalTransactionId,String reconciliationStatus,String externalSettlementReference,Instant createdAt,Instant completedAt,List<StatusEntry> history){
 public record StatusEntry(PaymentTransactionStatus status,String detail,Instant createdAt){}
}
