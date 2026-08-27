package com.nimbusbill.customer.repository;

import com.nimbusbill.customer.entity.Customer;
import com.nimbusbill.customer.entity.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Customer c where c.id=:id")
    java.util.Optional<Customer> findByIdForTransaction(@Param("id") UUID id);
    boolean existsByCustomerCodeIgnoreCase(String customerCode);
    boolean existsByCustomerCodeIgnoreCaseAndIdNot(String customerCode, UUID id);
    Page<Customer> findByCustomerNameContainingIgnoreCase(String search, Pageable pageable);
    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);
    Page<Customer> findByCustomerNameContainingIgnoreCaseAndStatus(String search, CustomerStatus status, Pageable pageable);
}
