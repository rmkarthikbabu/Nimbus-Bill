package com.nimbusbill.customer.controller;

import com.nimbusbill.customer.dto.AnalyticsReportResponse;
import com.nimbusbill.customer.service.AnalyticsReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/reports")
public class AnalyticsReportController {
    private final AnalyticsReportService service;
    public AnalyticsReportController(AnalyticsReportService service){this.service=service;}
    @GetMapping("/summary")
    public AnalyticsReportResponse summary(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fromDate,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate toDate,@RequestParam(required=false) UUID customerId){return service.report(fromDate,toDate,customerId);}
}
