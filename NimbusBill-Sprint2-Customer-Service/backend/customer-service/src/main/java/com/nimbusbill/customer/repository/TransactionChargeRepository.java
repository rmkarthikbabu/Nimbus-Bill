package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.TransactionCharge; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface TransactionChargeRepository extends JpaRepository<TransactionCharge,UUID>{Optional<TransactionCharge> findByTransactionId(UUID transactionId);}
