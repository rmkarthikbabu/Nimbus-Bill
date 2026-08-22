package com.nimbusbill.customer.dto;
import com.nimbusbill.customer.entity.PricingUnit; import com.nimbusbill.customer.entity.ProductStatus; import java.math.BigDecimal; import java.time.Instant; import java.time.LocalDate; import java.util.UUID;
public record ProductResponse(UUID id,String productCode,String productName,String description,PricingUnit pricingUnit,BigDecimal minimumFee,BigDecimal maximumFee,boolean taxApplicable,ProductStatus status,LocalDate effectiveFrom,LocalDate effectiveTo,Instant createdAt,Instant updatedAt,long version){}
