package com.nimbusbill.customer.repository; import com.nimbusbill.customer.entity.InvoiceLineItem; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem,UUID>{List<InvoiceLineItem> findByInvoiceIdOrderByProductCode(UUID invoiceId);}
