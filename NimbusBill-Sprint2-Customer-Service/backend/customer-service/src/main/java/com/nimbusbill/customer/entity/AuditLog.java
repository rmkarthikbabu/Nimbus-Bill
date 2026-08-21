package com.nimbusbill.customer.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name="customer_id",nullable=false) private UUID customerId;
    @Column(nullable=false,length=30) private String action;
    @Column(name="old_value",columnDefinition="TEXT") private String oldValue;
    @Column(name="new_value",columnDefinition="TEXT") private String newValue;
    @Column(nullable=false,length=150) private String actor;
    @Column(name="ip_address",length=64) private String ipAddress;
    @Column(name="created_at",nullable=false) private Instant createdAt;
    @PrePersist void prePersist(){if(createdAt==null)createdAt=Instant.now();}
    public UUID getId(){return id;} public UUID getCustomerId(){return customerId;} public void setCustomerId(UUID v){customerId=v;}
    public String getAction(){return action;} public void setAction(String v){action=v;} public String getOldValue(){return oldValue;} public void setOldValue(String v){oldValue=v;}
    public String getNewValue(){return newValue;} public void setNewValue(String v){newValue=v;} public String getActor(){return actor;} public void setActor(String v){actor=v;}
    public String getIpAddress(){return ipAddress;} public void setIpAddress(String v){ipAddress=v;} public Instant getCreatedAt(){return createdAt;}
}
