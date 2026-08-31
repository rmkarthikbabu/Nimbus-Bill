package com.nimbusbill.customer.dto; import java.time.Instant; import java.util.UUID;
public record InvoiceDeliveryResponse(UUID id,String channel,String destination,String status,int attemptCount,String lastError,Instant createdAt,Instant deliveredAt){}
