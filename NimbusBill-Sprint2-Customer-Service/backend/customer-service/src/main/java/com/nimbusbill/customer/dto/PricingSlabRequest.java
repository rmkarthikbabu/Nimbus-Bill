package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record PricingSlabRequest(@NotNull @DecimalMin("0") BigDecimal lowerBound,@DecimalMin("0") BigDecimal upperBound,@DecimalMin("0") BigDecimal flatFee,@DecimalMin("0") BigDecimal percentageRate,@Min(1) int sequenceNo){}
