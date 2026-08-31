package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.BillingPeriod; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface BillingPeriodRepository extends JpaRepository<BillingPeriod,UUID>{List<BillingPeriod> findByBillingAccountIdOrderByPeriodStartDesc(UUID accountId);}
