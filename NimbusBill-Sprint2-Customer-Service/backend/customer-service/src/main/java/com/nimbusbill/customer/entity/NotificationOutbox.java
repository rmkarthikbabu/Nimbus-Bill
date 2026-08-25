package com.nimbusbill.customer.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="notification_outbox")
public class NotificationOutbox {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @Column(name="event_type",nullable=false,length=60) private String eventType;
 @Column(name="entity_type",nullable=false,length=40) private String entityType;
 @Column(name="entity_id",nullable=false) private UUID entityId;
 @Column(name="recipient_role",nullable=false,length=60) private String recipientRole;
 @Column(nullable=false,length=200) private String subject;
 @Column(nullable=false,length=1000) private String message;
 @Column(nullable=false,length=20) private String status="PENDING";
 @Column(name="attempt_count",nullable=false) private int attemptCount;
 @Column(name="next_attempt_at") private Instant nextAttemptAt;
 @Column(name="last_error",length=1000) private String lastError;
 @Column(length=300) private String destination;
 @Column(name="created_at",nullable=false) private Instant createdAt;
 @Column(name="sent_at") private Instant sentAt;
 @PrePersist void create(){createdAt=Instant.now();}
 public UUID getId(){return id;} public String getEventType(){return eventType;} public void setEventType(String v){eventType=v;}
 public String getEntityType(){return entityType;} public void setEntityType(String v){entityType=v;} public UUID getEntityId(){return entityId;} public void setEntityId(UUID v){entityId=v;}
 public String getRecipientRole(){return recipientRole;} public void setRecipientRole(String v){recipientRole=v;} public String getSubject(){return subject;} public void setSubject(String v){subject=v;}
 public String getMessage(){return message;} public void setMessage(String v){message=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public int getAttemptCount(){return attemptCount;} public void setAttemptCount(int v){attemptCount=v;} public Instant getNextAttemptAt(){return nextAttemptAt;} public void setNextAttemptAt(Instant v){nextAttemptAt=v;}
 public String getLastError(){return lastError;} public void setLastError(String v){lastError=v;} public String getDestination(){return destination;} public void setDestination(String v){destination=v;}
 public Instant getCreatedAt(){return createdAt;} public Instant getSentAt(){return sentAt;} public void setSentAt(Instant v){sentAt=v;}
}
