package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.BillingRun; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface BillingRunRepository extends JpaRepository<BillingRun,UUID>{List<BillingRun> findByBillingPeriodBillingAccountCustomerIdOrderByStartedAtDesc(UUID customerId);}
