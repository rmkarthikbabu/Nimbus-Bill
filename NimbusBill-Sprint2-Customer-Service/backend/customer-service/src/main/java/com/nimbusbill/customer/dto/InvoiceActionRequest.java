package com.nimbusbill.customer.dto; import jakarta.validation.constraints.Size;
public record InvoiceActionRequest(@Size(max=500) String comment){}
