package com.nimbusbill.customer.service;
import com.nimbusbill.customer.entity.*; import com.nimbusbill.customer.repository.*; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDate;
@Service public class InvoiceAutomationService {
 private final InvoiceRepository invoices; private final InvoiceStatusHistoryRepository history; public InvoiceAutomationService(InvoiceRepository i,InvoiceStatusHistoryRepository h){invoices=i;history=h;}
 @Scheduled(cron="${app.invoice.overdue-cron:0 10 0 * * *}") @Transactional public void markOverdue(){for(Invoice i:invoices.findByStatusAndDueDateBefore("ISSUED",LocalDate.now())){i.setStatus("OVERDUE");invoices.save(i);InvoiceStatusHistory h=new InvoiceStatusHistory();h.setInvoice(i);h.setFromStatus("ISSUED");h.setToStatus("OVERDUE");h.setActor("invoice-scheduler");h.setComment("Due date passed");history.save(h);}}
}
