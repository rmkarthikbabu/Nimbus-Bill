package com.nimbusbill.customer.dto;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public record ReversalRequest(@NotBlank @Size(max=100) String clientReferenceId,@Size(max=500) String reason){}
