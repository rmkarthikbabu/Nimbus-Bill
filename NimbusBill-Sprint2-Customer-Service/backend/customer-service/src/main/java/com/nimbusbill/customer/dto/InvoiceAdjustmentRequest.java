package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record InvoiceAdjustmentRequest(@NotBlank @Pattern(regexp="CREDIT|DEBIT") String adjustmentType,@NotNull @DecimalMin("0.0001") BigDecimal amount,@NotBlank @Size(max=300) String reason){}
