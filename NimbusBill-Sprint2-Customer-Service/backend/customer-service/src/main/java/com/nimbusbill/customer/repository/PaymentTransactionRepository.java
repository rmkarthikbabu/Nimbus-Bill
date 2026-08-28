package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.time.Instant; import java.util.*;
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,UUID>,JpaSpecificationExecutor<PaymentTransaction>{
 Optional<PaymentTransaction> findByCustomerIdAndClientReferenceIdAndTransactionType(UUID customerId,String reference,String transactionType);
 List<PaymentTransaction> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
 Optional<PaymentTransaction> findByOriginalTransactionId(UUID originalTransactionId);
 @Query("select coalesce(sum(t.amount),0) from PaymentTransaction t where t.customer.id=:customerId and upper(t.productCode)=upper(:product) and t.transactionKind='PAYMENT' and t.status='COMPLETED' and t.createdAt>=:fromDate and t.createdAt<:toDate")
 java.math.BigDecimal completedAmount(@Param("customerId") UUID customerId,@Param("product") String product,@Param("fromDate") Instant fromDate,@Param("toDate") Instant toDate);
}
