package com.nimbusbill.customer.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.util.UUID;
public record PaymentTransactionRequest(
 @NotNull UUID customerId,
 @NotBlank @Size(max=100) String clientReferenceId,
 @NotBlank @Size(max=30) String productCode,
 @NotBlank @Size(max=60) String transactionType,
 @NotNull @DecimalMin(value="0.0001") BigDecimal amount,
 @NotBlank @Pattern(regexp="[A-Za-z]{3}") String currency,
 @Size(max=100) String sourceAccount,
 @Size(max=100) String destinationAccount){}
