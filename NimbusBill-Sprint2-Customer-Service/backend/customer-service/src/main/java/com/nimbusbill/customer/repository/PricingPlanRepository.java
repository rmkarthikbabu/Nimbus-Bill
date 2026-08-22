package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.PricingPlan; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PricingPlanRepository extends JpaRepository<PricingPlan,UUID>{boolean existsByPlanCodeIgnoreCase(String code);List<PricingPlan> findAllByOrderByPlanName();}
