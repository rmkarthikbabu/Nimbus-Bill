package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*; import java.time.LocalDate; import java.util.UUID;
public record CustomerProductRequest(@NotNull UUID productId,boolean enabled,@NotNull LocalDate activationDate,LocalDate expiryDate){}
