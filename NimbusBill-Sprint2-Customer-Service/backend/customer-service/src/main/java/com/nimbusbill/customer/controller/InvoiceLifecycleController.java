package com.nimbusbill.customer.controller;
import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.service.InvoiceLifecycleService; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1") public class InvoiceLifecycleController {
 private final InvoiceLifecycleService service; public InvoiceLifecycleController(InvoiceLifecycleService s){service=s;}
 @PutMapping("/customers/{id}/tax-profile") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE')") TaxProfileResponse saveTax(@PathVariable UUID id,@Valid @RequestBody TaxProfileRequest r){return service.saveTax(id,r);}
 @GetMapping("/customers/{id}/tax-profile") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") TaxProfileResponse tax(@PathVariable UUID id){return service.getTax(id);}
 @PostMapping("/invoices/{id}/adjustments") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") void adjust(@PathVariable UUID id,@Valid @RequestBody InvoiceAdjustmentRequest r){service.adjust(id,r);}
 @PostMapping("/invoices/{id}/reject") @PreAuthorize("hasAnyRole('ADMIN','FINANCE')") void reject(@PathVariable UUID id,@RequestBody(required=false) InvoiceActionRequest r){service.reject(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/reopen") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") void reopen(@PathVariable UUID id,@RequestBody(required=false) InvoiceActionRequest r){service.reopen(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/dispute") @PreAuthorize("hasAnyRole('ADMIN','FINANCE')") void dispute(@PathVariable UUID id,@RequestBody(required=false) InvoiceActionRequest r){service.dispute(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/paid") @PreAuthorize("hasAnyRole('ADMIN','FINANCE')") void paid(@PathVariable UUID id,@RequestBody(required=false) InvoiceActionRequest r){service.markPaid(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/deliveries") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") InvoiceDeliveryResponse deliver(@PathVariable UUID id,@Valid @RequestBody InvoiceDeliveryRequest r){return service.deliver(id,r);}
 @GetMapping("/invoices/{id}/deliveries") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") List<InvoiceDeliveryResponse> deliveries(@PathVariable UUID id){return service.deliveries(id);}
}
