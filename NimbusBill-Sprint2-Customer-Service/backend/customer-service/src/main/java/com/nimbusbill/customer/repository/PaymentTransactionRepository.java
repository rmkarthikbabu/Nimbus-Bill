package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.time.Instant; import java.util.*;
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,UUID>{
 Optional<PaymentTransaction> findByCustomerIdAndClientReferenceIdAndTransactionType(UUID customerId,String reference,String transactionType);
 List<PaymentTransaction> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
 Optional<PaymentTransaction> findByOriginalTransactionId(UUID originalTransactionId);
 @Query("select coalesce(sum(t.amount),0) from PaymentTransaction t where t.customer.id=:customerId and upper(t.productCode)=upper(:product) and t.transactionKind='PAYMENT' and t.status='COMPLETED' and t.createdAt>=:fromDate and t.createdAt<:toDate")
 java.math.BigDecimal completedAmount(@Param("customerId") UUID customerId,@Param("product") String product,@Param("fromDate") Instant fromDate,@Param("toDate") Instant toDate);
 @Query("select t from PaymentTransaction t where t.customer.id=:customerId and (:status is null or t.status=:status) and (:product is null or upper(t.productCode)=upper(:product)) and (:reference is null or lower(t.clientReferenceId) like lower(concat('%',:reference,'%'))) and (:fromDate is null or t.createdAt>=:fromDate) and (:toDate is null or t.createdAt<:toDate) order by t.createdAt desc")
 List<PaymentTransaction> search(@Param("customerId") UUID customerId,@Param("status") PaymentTransactionStatus status,@Param("product") String product,@Param("reference") String reference,@Param("fromDate") Instant fromDate,@Param("toDate") Instant toDate);
}
