package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="invoice_status_history") public class InvoiceStatusHistory {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id; @ManyToOne(optional=false) @JoinColumn(name="invoice_id") private Invoice invoice; @Column(name="from_status",length=20) private String fromStatus; @Column(name="to_status",nullable=false,length=20) private String toStatus; @Column(nullable=false,length=100) private String actor; @Column(length=500) private String comment; @Column(name="created_at",nullable=false) private Instant createdAt; @PrePersist void create(){createdAt=Instant.now();}
 public void setInvoice(Invoice v){invoice=v;} public String getFromStatus(){return fromStatus;} public void setFromStatus(String v){fromStatus=v;} public String getToStatus(){return toStatus;} public void setToStatus(String v){toStatus=v;} public String getActor(){return actor;} public void setActor(String v){actor=v;} public String getComment(){return comment;} public void setComment(String v){comment=v;} public Instant getCreatedAt(){return createdAt;}
}
