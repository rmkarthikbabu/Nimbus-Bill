package com.nimbusbill.customer.dto; import java.time.LocalDate; import java.util.UUID;
public record CustomerProductResponse(UUID id,UUID productId,String productCode,String productName,boolean enabled,LocalDate activationDate,LocalDate expiryDate,UUID billingAccountId,java.math.BigDecimal transactionLimit,java.math.BigDecimal dailyLimit){}
