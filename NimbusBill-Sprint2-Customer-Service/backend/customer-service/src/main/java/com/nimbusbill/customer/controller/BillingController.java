package com.nimbusbill.customer.controller;
import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.service.BillingService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.net.URI; import java.util.*;
@RestController @RequestMapping("/api/v1") public class BillingController {
 private final BillingService service; public BillingController(BillingService service){this.service=service;}
 @PostMapping("/billing-accounts") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") ResponseEntity<BillingAccountResponse> account(@Valid @RequestBody BillingAccountRequest request){var result=service.createAccount(request);return ResponseEntity.created(URI.create("/api/v1/billing-accounts/"+result.id())).body(result);}
 @GetMapping("/billing-accounts") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','OPERATIONS','AUDITOR','READ_ONLY')") List<BillingAccountResponse> accounts(@RequestParam UUID customerId){return service.listAccounts(customerId);}
 @PostMapping("/billing-periods") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") ResponseEntity<BillingPeriodResponse> period(@Valid @RequestBody BillingPeriodRequest request){var result=service.createPeriod(request);return ResponseEntity.created(URI.create("/api/v1/billing-periods/"+result.id())).body(result);}
 @GetMapping("/billing-periods") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") List<BillingPeriodResponse> periods(@RequestParam UUID billingAccountId){return service.listPeriods(billingAccountId);}
 @PostMapping("/billing-periods/{id}/preview") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE')") BillingRunResponse preview(@PathVariable UUID id){return service.preview(id);}
 @PostMapping("/billing-runs/{id}/execute") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") BillingRunResponse execute(@PathVariable UUID id){return service.execute(id);}
 @GetMapping("/billing-runs") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") List<BillingRunResponse> runs(@RequestParam UUID customerId){return service.listRuns(customerId);}
}
