package com.nimbusbill.customer.dto;

import com.nimbusbill.customer.entity.BillingCycle;
import com.nimbusbill.customer.entity.CustomerStatus;
import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String customerCode,
        String customerName,
        String legalName,
        String customerType,
        String industry,
        String country,
        String currency,
        BillingCycle billingCycle,
        CustomerStatus status,
        String taxIdentifier,
        String website,
        Instant createdAt,
        Instant updatedAt,
        long version
) {}
