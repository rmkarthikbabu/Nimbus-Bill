package com.nimbusbill.customer.dto; import java.math.BigDecimal; import java.time.LocalDate; import java.util.UUID;
public record TaxProfileResponse(UUID id,UUID customerId,String taxIdentifier,String taxCountry,String taxRegion,String placeOfSupply,BigDecimal defaultTaxRate,boolean taxExempt,LocalDate effectiveFrom){}
