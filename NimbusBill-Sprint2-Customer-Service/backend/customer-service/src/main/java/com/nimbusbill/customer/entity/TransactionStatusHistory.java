package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="transaction_status_history") public class TransactionStatusHistory {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(optional=false) @JoinColumn(name="transaction_id") private PaymentTransaction transaction;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private PaymentTransactionStatus status;
 @Column(length=500) private String detail; @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @PrePersist void create(){createdAt=Instant.now();} public UUID getId(){return id;} public PaymentTransaction getTransaction(){return transaction;}
 public void setTransaction(PaymentTransaction v){transaction=v;} public PaymentTransactionStatus getStatus(){return status;} public void setStatus(PaymentTransactionStatus v){status=v;}
 public String getDetail(){return detail;} public void setDetail(String v){detail=v;} public Instant getCreatedAt(){return createdAt;}
}
