package com.nimbusbill.customer.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.time.LocalDate; import java.util.*;
@Entity @Table(name="payment_products")
public class PaymentProduct {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @Column(name="product_code",nullable=false,unique=true,length=30) private String productCode;
 @Column(name="product_name",nullable=false,length=120) private String productName;
 @Column(length=500) private String description;
 @Enumerated(EnumType.STRING) @Column(name="pricing_unit",nullable=false,length=30) private PricingUnit pricingUnit;
 @Column(name="minimum_fee",precision=19,scale=4) private BigDecimal minimumFee;
 @Column(name="maximum_fee",precision=19,scale=4) private BigDecimal maximumFee;
 @Column(name="tax_applicable",nullable=false) private boolean taxApplicable;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private ProductStatus status;
 @Column(name="effective_from",nullable=false) private LocalDate effectiveFrom;
 @Column(name="effective_to") private LocalDate effectiveTo;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @Column(name="updated_at",nullable=false) private Instant updatedAt;
 @Version private long version;
 @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="product_transaction_types",joinColumns=@JoinColumn(name="product_id")) @Column(name="transaction_type",nullable=false,length=60) private Set<String> transactionTypes=new LinkedHashSet<>();
 @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="product_currencies",joinColumns=@JoinColumn(name="product_id")) @Column(name="currency",nullable=false,length=3) private Set<String> currencies=new LinkedHashSet<>();
 @PrePersist void create(){Instant now=Instant.now();createdAt=now;updatedAt=now;if(status==null)status=ProductStatus.DRAFT;}
 @PreUpdate void update(){updatedAt=Instant.now();}
 public UUID getId(){return id;} public String getProductCode(){return productCode;} public void setProductCode(String v){productCode=v;} public String getProductName(){return productName;} public void setProductName(String v){productName=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public PricingUnit getPricingUnit(){return pricingUnit;} public void setPricingUnit(PricingUnit v){pricingUnit=v;} public BigDecimal getMinimumFee(){return minimumFee;} public void setMinimumFee(BigDecimal v){minimumFee=v;} public BigDecimal getMaximumFee(){return maximumFee;} public void setMaximumFee(BigDecimal v){maximumFee=v;} public boolean isTaxApplicable(){return taxApplicable;} public void setTaxApplicable(boolean v){taxApplicable=v;} public ProductStatus getStatus(){return status;} public void setStatus(ProductStatus v){status=v;} public LocalDate getEffectiveFrom(){return effectiveFrom;} public void setEffectiveFrom(LocalDate v){effectiveFrom=v;} public LocalDate getEffectiveTo(){return effectiveTo;} public void setEffectiveTo(LocalDate v){effectiveTo=v;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public long getVersion(){return version;} public Set<String> getTransactionTypes(){return transactionTypes;} public Set<String> getCurrencies(){return currencies;}
}
