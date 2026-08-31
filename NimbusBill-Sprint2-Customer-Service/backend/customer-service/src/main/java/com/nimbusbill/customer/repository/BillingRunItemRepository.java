package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.BillingRunItem; import org.springframework.data.jpa.repository.JpaRepository; import java.util.UUID;
public interface BillingRunItemRepository extends JpaRepository<BillingRunItem,UUID>{java.util.List<BillingRunItem> findByBillingRunIdOrderByProductCode(UUID billingRunId);}
