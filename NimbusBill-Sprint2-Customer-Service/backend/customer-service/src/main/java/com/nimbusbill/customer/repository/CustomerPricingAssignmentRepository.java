package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.CustomerPricingAssignment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CustomerPricingAssignmentRepository extends JpaRepository<CustomerPricingAssignment,UUID>{List<CustomerPricingAssignment> findByCustomerIdOrderByEffectiveFromDesc(UUID customerId);}
