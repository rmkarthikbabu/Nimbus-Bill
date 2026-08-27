package com.nimbusbill.customer.dto;
import jakarta.validation.constraints.*;
public record ReconciliationRequest(@NotBlank @Pattern(regexp="MATCHED|EXCEPTION") String status,@Size(max=100) String externalSettlementReference,@Size(max=500) String detail){}
