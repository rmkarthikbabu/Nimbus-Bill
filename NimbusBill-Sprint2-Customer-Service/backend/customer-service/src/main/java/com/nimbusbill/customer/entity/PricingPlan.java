package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.*;
@Entity @Table(name="pricing_plans") public class PricingPlan{
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @Column(name="plan_code",nullable=false,unique=true,length=30) private String planCode; @Column(name="plan_name",nullable=false,length=120) private String planName; @Column(length=500) private String description; @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt; @Version private long version;
 @PrePersist void create(){createdAt=Instant.now();updatedAt=createdAt;} @PreUpdate void update(){updatedAt=Instant.now();}
 public UUID getId(){return id;} public String getPlanCode(){return planCode;} public void setPlanCode(String v){planCode=v;} public String getPlanName(){return planName;} public void setPlanName(String v){planName=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public long getVersion(){return version;}
}
