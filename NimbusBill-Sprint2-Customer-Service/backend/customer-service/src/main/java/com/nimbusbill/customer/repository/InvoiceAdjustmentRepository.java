package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.InvoiceAdjustment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface InvoiceAdjustmentRepository extends JpaRepository<InvoiceAdjustment,UUID>{List<InvoiceAdjustment> findByInvoiceId(UUID invoiceId);}
