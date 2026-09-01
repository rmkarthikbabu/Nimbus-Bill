package com.nimbusbill.customer.dto; import java.math.BigDecimal;
public record InvoiceReportResponse(long totalInvoices,long draft,long issued,long paid,long overdue,long disputed,BigDecimal invoicedAmount,BigDecimal outstandingAmount,BigDecimal taxAmount){}
