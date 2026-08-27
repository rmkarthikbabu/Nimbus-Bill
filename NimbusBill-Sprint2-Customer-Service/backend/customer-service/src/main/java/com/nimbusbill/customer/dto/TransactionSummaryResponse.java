package com.nimbusbill.customer.dto;
import java.math.BigDecimal;
public record TransactionSummaryResponse(long total,long completed,long rejected,long pendingReconciliation,long reconciliationExceptions,BigDecimal completedAmount,BigDecimal totalCharges){}
