package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="transaction_charges") public class TransactionCharge {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @OneToOne(optional=false) @JoinColumn(name="transaction_id",unique=true) private PaymentTransaction transaction;
 @ManyToOne(optional=false) @JoinColumn(name="customer_id") private Customer customer;
 @Column(name="pricing_plan_version_id",nullable=false) private UUID pricingPlanVersionId;
 @Column(name="base_fee",nullable=false,precision=19,scale=4) private BigDecimal baseFee;
 @Column(name="tax_amount",nullable=false,precision=19,scale=4) private BigDecimal taxAmount;
 @Column(name="charge_amount",nullable=false,precision=19,scale=4) private BigDecimal chargeAmount;
 @Column(nullable=false,length=3) private String currency; @Column(nullable=false,length=20) private String status="PENDING";
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @PrePersist void create(){createdAt=Instant.now();}
 public UUID getId(){return id;} public void setTransaction(PaymentTransaction v){transaction=v;} public void setCustomer(Customer v){customer=v;}
 public UUID getPricingPlanVersionId(){return pricingPlanVersionId;} public void setPricingPlanVersionId(UUID v){pricingPlanVersionId=v;}
 public BigDecimal getBaseFee(){return baseFee;} public void setBaseFee(BigDecimal v){baseFee=v;} public BigDecimal getTaxAmount(){return taxAmount;} public void setTaxAmount(BigDecimal v){taxAmount=v;}
 public BigDecimal getChargeAmount(){return chargeAmount;} public void setChargeAmount(BigDecimal v){chargeAmount=v;} public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;} public PaymentTransaction getTransaction(){return transaction;}
}
