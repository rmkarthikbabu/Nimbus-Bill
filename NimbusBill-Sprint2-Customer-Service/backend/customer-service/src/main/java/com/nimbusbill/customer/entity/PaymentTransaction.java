package com.nimbusbill.customer.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions", uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "client_reference_id", "transaction_type"}))
public class PaymentTransaction {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @ManyToOne(optional = false) @JoinColumn(name = "customer_id") private Customer customer;
    @Column(name = "client_reference_id", nullable = false, length = 100) private String clientReferenceId;
    @Column(name = "product_code", nullable = false, length = 30) private String productCode;
    @Column(name = "transaction_type", nullable = false, length = 60) private String transactionType;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Column(name = "source_account", length = 100) private String sourceAccount;
    @Column(name = "destination_account", length = 100) private String destinationAccount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private PaymentTransactionStatus status;
    @Column(name = "failure_reason", length = 500) private String failureReason;
    @Column(name="transaction_kind",nullable=false,length=20) private String transactionKind="PAYMENT";
    @Column(name="ingestion_source",nullable=false,length=20) private String ingestionSource="REST";
    @ManyToOne @JoinColumn(name="original_transaction_id") private PaymentTransaction originalTransaction;
    @Column(name="reconciliation_status",nullable=false,length=20) private String reconciliationStatus="PENDING";
    @Column(name="external_settlement_reference",length=100) private String externalSettlementReference;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @Column(name = "completed_at") private Instant completedAt;
    @Version private long version;
    @PrePersist void create(){var now=Instant.now();createdAt=now;updatedAt=now;if(status==null)status=PaymentTransactionStatus.RECEIVED;}
    @PreUpdate void update(){updatedAt=Instant.now();}
    public UUID getId(){return id;} public Customer getCustomer(){return customer;} public void setCustomer(Customer v){customer=v;}
    public String getClientReferenceId(){return clientReferenceId;} public void setClientReferenceId(String v){clientReferenceId=v;}
    public String getProductCode(){return productCode;} public void setProductCode(String v){productCode=v;}
    public String getTransactionType(){return transactionType;} public void setTransactionType(String v){transactionType=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;}
    public String getSourceAccount(){return sourceAccount;} public void setSourceAccount(String v){sourceAccount=v;}
    public String getDestinationAccount(){return destinationAccount;} public void setDestinationAccount(String v){destinationAccount=v;}
    public PaymentTransactionStatus getStatus(){return status;} public void setStatus(PaymentTransactionStatus v){status=v;}
    public String getFailureReason(){return failureReason;} public void setFailureReason(String v){failureReason=v;}
    public String getTransactionKind(){return transactionKind;} public void setTransactionKind(String v){transactionKind=v;} public String getIngestionSource(){return ingestionSource;} public void setIngestionSource(String v){ingestionSource=v;}
    public PaymentTransaction getOriginalTransaction(){return originalTransaction;} public void setOriginalTransaction(PaymentTransaction v){originalTransaction=v;}
    public String getReconciliationStatus(){return reconciliationStatus;} public void setReconciliationStatus(String v){reconciliationStatus=v;} public String getExternalSettlementReference(){return externalSettlementReference;} public void setExternalSettlementReference(String v){externalSettlementReference=v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
    public Instant getCompletedAt(){return completedAt;} public void setCompletedAt(Instant v){completedAt=v;}
}
