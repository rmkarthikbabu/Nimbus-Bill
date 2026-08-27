package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.TransactionStatusHistory; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface TransactionStatusHistoryRepository extends JpaRepository<TransactionStatusHistory,UUID>{List<TransactionStatusHistory> findByTransactionIdOrderByCreatedAt(UUID transactionId);}
