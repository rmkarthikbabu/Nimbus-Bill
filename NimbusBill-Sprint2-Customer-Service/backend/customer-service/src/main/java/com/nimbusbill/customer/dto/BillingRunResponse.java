package com.nimbusbill.customer.dto; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
public record BillingRunResponse(UUID id,UUID billingPeriodId,UUID billingAccountId,String status,int chargeCount,BigDecimal subtotal,BigDecimal taxTotal,BigDecimal grandTotal,Instant startedAt,Instant completedAt,String failureReason){}
