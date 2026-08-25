package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*; import java.time.LocalDate; import java.util.UUID;
public record CustomerPricingRequest(@NotNull UUID planId,@NotNull LocalDate effectiveFrom,LocalDate effectiveTo,UUID billingAccountId,UUID productId){}
