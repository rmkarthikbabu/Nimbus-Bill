package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.InvoiceStatusHistory; import org.springframework.data.jpa.repository.JpaRepository; import java.util.UUID;
public interface InvoiceStatusHistoryRepository extends JpaRepository<InvoiceStatusHistory,UUID>{}
