package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.PaymentProduct; import com.nimbusbill.customer.entity.ProductStatus; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List; import java.util.UUID;
public interface PaymentProductRepository extends JpaRepository<PaymentProduct,UUID>{boolean existsByProductCodeIgnoreCase(String code);boolean existsByProductCodeIgnoreCaseAndIdNot(String code,UUID id);List<PaymentProduct> findByStatusOrderByProductName(ProductStatus status);List<PaymentProduct> findAllByOrderByProductName();}
