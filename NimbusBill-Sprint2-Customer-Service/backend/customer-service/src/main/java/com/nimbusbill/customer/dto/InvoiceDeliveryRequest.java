package com.nimbusbill.customer.dto; import jakarta.validation.constraints.*;
public record InvoiceDeliveryRequest(@NotBlank @Pattern(regexp="EMAIL|WEBHOOK|API") String channel,@NotBlank @Size(max=255) String destination){}
