package com.nimbusbill.customer.dto;
import java.time.Instant; import java.util.UUID;
public record TransactionOutboxResponse(UUID id,UUID transactionId,String eventType,String status,int attemptCount,String lastError,Instant nextAttemptAt,Instant createdAt,Instant publishedAt){}
