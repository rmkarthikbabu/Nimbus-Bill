package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.CustomerTaxProfile; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CustomerTaxProfileRepository extends JpaRepository<CustomerTaxProfile,UUID>{Optional<CustomerTaxProfile> findByCustomerId(UUID customerId);}
