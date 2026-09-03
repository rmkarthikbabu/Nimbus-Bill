package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*; import java.util.UUID; public record AiAssistantRequest(@NotBlank @Size(max=1000) String question,UUID customerId){}
