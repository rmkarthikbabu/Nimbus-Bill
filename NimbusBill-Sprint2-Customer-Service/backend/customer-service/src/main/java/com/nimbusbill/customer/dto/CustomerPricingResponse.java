package com.nimbusbill.customer.dto; import java.time.LocalDate; import java.util.UUID;
public record CustomerPricingResponse(UUID id,UUID planId,String planCode,String planName,LocalDate effectiveFrom,LocalDate effectiveTo,boolean active){}
