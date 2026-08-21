package com.nimbusbill.customer.dto;

import com.nimbusbill.customer.entity.BillingCycle;
import com.nimbusbill.customer.entity.CustomerStatus;
import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotBlank @Size(max = 30) @Pattern(regexp = "^[A-Z0-9_-]+$") String customerCode,
        @NotBlank @Size(max = 150) String customerName,
        @NotBlank @Size(max = 180) String legalName,
        @NotBlank @Size(max = 40) String customerType,
        @Size(max = 80) String industry,
        @NotBlank @Pattern(regexp = "^[A-Z]{2}$", message = "country must be an ISO alpha-2 code") String country,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be an ISO alpha-3 code") String currency,
        @NotNull BillingCycle billingCycle,
        CustomerStatus status,
        @Size(max = 50) String taxIdentifier,
        @Size(max = 200) String website
) {}
