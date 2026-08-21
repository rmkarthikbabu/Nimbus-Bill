package com.nimbusbill.customer.dto;
import java.time.Instant; import java.util.UUID;
public record AuditLogResponse(UUID id,UUID customerId,String action,String oldValue,String newValue,String actor,String ipAddress,Instant createdAt){}
