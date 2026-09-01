package com.nimbusbill.customer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record AnalyticsReportResponse(
        LocalDate fromDate,
        LocalDate toDate,
        long totalCustomers,
        long activeCustomers,
        long totalTransactions,
        long completedTransactions,
        long rejectedTransactions,
        BigDecimal transactionValue,
        long totalInvoices,
        long overdueInvoices,
        BigDecimal invoicedRevenue,
        BigDecimal collectedRevenue,
        Map<String, Long> transactionsByProduct,
        Map<String, Long> invoicesByStatus) {}
