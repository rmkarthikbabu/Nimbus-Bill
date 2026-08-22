package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.LocalDate; import java.util.UUID;
public record PricingPreviewRequest(@NotNull UUID planId,@NotBlank String productCode,@NotNull @DecimalMin("0.01") BigDecimal amount,@NotNull LocalDate transactionDate){}
