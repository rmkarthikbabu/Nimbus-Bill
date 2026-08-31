package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.time.*; import java.util.UUID;
@Entity @Table(name="billing_periods") public class BillingPeriod {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @ManyToOne(optional=false) @JoinColumn(name="billing_account_id") private BillingAccount billingAccount;
 @Column(name="period_start",nullable=false) private LocalDate periodStart; @Column(name="period_end",nullable=false) private LocalDate periodEnd; @Column(nullable=false,length=20) private String status="OPEN";
 @Column(name="closed_at") private Instant closedAt; @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @PrePersist void create(){createdAt=Instant.now();}
 public UUID getId(){return id;} public BillingAccount getBillingAccount(){return billingAccount;} public void setBillingAccount(BillingAccount v){billingAccount=v;} public LocalDate getPeriodStart(){return periodStart;} public void setPeriodStart(LocalDate v){periodStart=v;} public LocalDate getPeriodEnd(){return periodEnd;} public void setPeriodEnd(LocalDate v){periodEnd=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public Instant getClosedAt(){return closedAt;} public void setClosedAt(Instant v){closedAt=v;} public Instant getCreatedAt(){return createdAt;}
}
