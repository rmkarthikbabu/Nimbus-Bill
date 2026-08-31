package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="invoice_adjustments") public class InvoiceAdjustment {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @ManyToOne(optional=false) @JoinColumn(name="invoice_id") private Invoice invoice; @Column(name="adjustment_type",nullable=false,length=10) private String adjustmentType; @Column(nullable=false,precision=19,scale=4) private BigDecimal amount; @Column(nullable=false,length=300) private String reason; @Column(name="created_by",nullable=false,length=100) private String createdBy; @Column(name="created_at",nullable=false) private Instant createdAt; @PrePersist void create(){createdAt=Instant.now();}
 public void setInvoice(Invoice v){invoice=v;} public String getAdjustmentType(){return adjustmentType;} public void setAdjustmentType(String v){adjustmentType=v;} public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;} public void setReason(String v){reason=v;} public void setCreatedBy(String v){createdBy=v;}
}
