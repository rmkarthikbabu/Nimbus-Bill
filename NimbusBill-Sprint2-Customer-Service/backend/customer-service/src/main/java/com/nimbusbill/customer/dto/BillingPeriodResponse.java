package com.nimbusbill.customer.dto; import java.time.*; import java.util.UUID;
public record BillingPeriodResponse(UUID id,UUID billingAccountId,LocalDate periodStart,LocalDate periodEnd,String status,Instant closedAt){}
