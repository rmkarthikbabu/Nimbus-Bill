package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="transaction_outbox") public class TransactionOutbox {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @ManyToOne(optional=false) @JoinColumn(name="transaction_id") private PaymentTransaction transaction;
 @Column(name="event_type",nullable=false,length=60) private String eventType; @Column(nullable=false,columnDefinition="TEXT") private String payload;
 @Column(nullable=false,length=20) private String status="PENDING"; @Column(name="attempt_count",nullable=false) private int attemptCount;
 @Column(name="next_attempt_at") private Instant nextAttemptAt; @Column(name="last_error",length=1000) private String lastError;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="published_at") private Instant publishedAt;
 @PrePersist void create(){createdAt=Instant.now();} public UUID getId(){return id;} public void setTransaction(PaymentTransaction v){transaction=v;}
 public PaymentTransaction getTransaction(){return transaction;} public String getEventType(){return eventType;} public void setEventType(String v){eventType=v;} public String getPayload(){return payload;} public void setPayload(String v){payload=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public int getAttemptCount(){return attemptCount;} public void setAttemptCount(int v){attemptCount=v;} public Instant getNextAttemptAt(){return nextAttemptAt;} public void setNextAttemptAt(Instant v){nextAttemptAt=v;} public String getLastError(){return lastError;} public void setLastError(String v){lastError=v;} public Instant getCreatedAt(){return createdAt;} public Instant getPublishedAt(){return publishedAt;} public void setPublishedAt(Instant v){publishedAt=v;}
}
