package com.nimbusbill.customer.dto; import jakarta.validation.constraints.NotNull; import java.time.LocalDate; import java.util.UUID;
public record BillingPeriodRequest(@NotNull UUID billingAccountId,@NotNull LocalDate periodStart,@NotNull LocalDate periodEnd){}
