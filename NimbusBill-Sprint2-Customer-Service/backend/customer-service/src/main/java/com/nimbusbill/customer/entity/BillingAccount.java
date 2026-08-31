package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="billing_accounts") public class BillingAccount {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @ManyToOne(optional=false) @JoinColumn(name="customer_id") private Customer customer;
 @Column(name="account_code",nullable=false,length=40) private String accountCode; @Column(name="account_name",nullable=false,length=150) private String accountName;
 @Column(nullable=false,length=3) private String currency; @Enumerated(EnumType.STRING) @Column(name="billing_cycle",nullable=false,length=20) private BillingCycle billingCycle;
 @Column(name="payment_terms_days",nullable=false) private int paymentTermsDays=30; @Column(nullable=false,length=20) private String status="ACTIVE";
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 @PrePersist void create(){createdAt=Instant.now();updatedAt=createdAt;} @PreUpdate void update(){updatedAt=Instant.now();}
 public UUID getId(){return id;} public Customer getCustomer(){return customer;} public void setCustomer(Customer v){customer=v;} public String getAccountCode(){return accountCode;} public void setAccountCode(String v){accountCode=v;} public String getAccountName(){return accountName;} public void setAccountName(String v){accountName=v;} public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;} public BillingCycle getBillingCycle(){return billingCycle;} public void setBillingCycle(BillingCycle v){billingCycle=v;} public int getPaymentTermsDays(){return paymentTermsDays;} public void setPaymentTermsDays(int v){paymentTermsDays=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public Instant getCreatedAt(){return createdAt;}
}
