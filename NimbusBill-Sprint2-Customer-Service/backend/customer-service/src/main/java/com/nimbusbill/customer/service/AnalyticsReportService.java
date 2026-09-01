package com.nimbusbill.customer.service;

import com.nimbusbill.customer.dto.AnalyticsReportResponse;
import com.nimbusbill.customer.entity.*;
import com.nimbusbill.customer.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsReportService {
    private final CustomerRepository customers;
    private final PaymentTransactionRepository transactions;
    private final InvoiceRepository invoices;
    public AnalyticsReportService(CustomerRepository customers, PaymentTransactionRepository transactions, InvoiceRepository invoices) {
        this.customers=customers; this.transactions=transactions; this.invoices=invoices;
    }
    @Transactional(readOnly=true)
    public AnalyticsReportResponse report(LocalDate from, LocalDate to, UUID customerId) {
        LocalDate end=to==null?LocalDate.now():to; LocalDate start=from==null?end.minusDays(29):from;
        if(start.isAfter(end)) throw new IllegalArgumentException("fromDate cannot be after toDate");
        Instant fromInstant=start.atStartOfDay(ZoneOffset.UTC).toInstant(); Instant toInstant=end.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        var tx=transactions.findAll().stream().filter(t->customerId==null||t.getCustomer().getId().equals(customerId)).filter(t->!t.getCreatedAt().isBefore(fromInstant)&&t.getCreatedAt().isBefore(toInstant)).toList();
        var inv=invoices.findAll().stream().filter(i->customerId==null||i.getCustomer().getId().equals(customerId)).filter(i->!i.getInvoiceDate().isBefore(start)&&!i.getInvoiceDate().isAfter(end)).toList();
        var scopedCustomers=customers.findAll().stream().filter(c->customerId==null||c.getId().equals(customerId)).toList();
        Map<String,Long> byProduct=tx.stream().collect(Collectors.groupingBy(PaymentTransaction::getProductCode,TreeMap::new,Collectors.counting()));
        Map<String,Long> byStatus=inv.stream().collect(Collectors.groupingBy(Invoice::getStatus,TreeMap::new,Collectors.counting()));
        BigDecimal value=tx.stream().filter(t->t.getStatus()==PaymentTransactionStatus.COMPLETED).map(PaymentTransaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal invoiced=inv.stream().filter(i->Set.of("ISSUED","OVERDUE","PAID","DISPUTED").contains(i.getStatus())).map(Invoice::getGrandTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal collected=inv.stream().filter(i->"PAID".equals(i.getStatus())).map(Invoice::getGrandTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
        return new AnalyticsReportResponse(start,end,scopedCustomers.size(),scopedCustomers.stream().filter(c->c.getStatus()==CustomerStatus.ACTIVE).count(),tx.size(),tx.stream().filter(t->t.getStatus()==PaymentTransactionStatus.COMPLETED).count(),tx.stream().filter(t->t.getStatus()==PaymentTransactionStatus.REJECTED).count(),value,inv.size(),inv.stream().filter(i->"OVERDUE".equals(i.getStatus())).count(),invoiced,collected,byProduct,byStatus);
    }
}
