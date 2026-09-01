package com.nimbusbill.customer.controller;
import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.service.*; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1") public class InvoiceController {
 private final InvoiceService service; private final InvoiceDocumentService documents; public InvoiceController(InvoiceService s,InvoiceDocumentService d){service=s;documents=d;}
 @PostMapping("/billing-runs/{id}/invoice") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") InvoiceResponse generate(@PathVariable UUID id){return service.generate(id);}
 @GetMapping("/invoices") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") List<InvoiceResponse> list(@RequestParam UUID customerId){return service.list(customerId);}
 @GetMapping("/invoices/{id}") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") InvoiceResponse get(@PathVariable UUID id){return service.get(id);}
 @PostMapping("/invoices/{id}/submit") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") InvoiceResponse submit(@PathVariable UUID id,@Valid @RequestBody(required=false) InvoiceActionRequest r){return service.submit(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/approve") @PreAuthorize("hasAnyRole('ADMIN','FINANCE')") InvoiceResponse approve(@PathVariable UUID id,@Valid @RequestBody(required=false) InvoiceActionRequest r){return service.approve(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/issue") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") InvoiceResponse issue(@PathVariable UUID id,@Valid @RequestBody(required=false) InvoiceActionRequest r){return service.issue(id,r==null?null:r.comment());}
 @PostMapping("/invoices/{id}/cancel") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") InvoiceResponse cancel(@PathVariable UUID id,@Valid @RequestBody(required=false) InvoiceActionRequest r){return service.cancel(id,r==null?null:r.comment());}
 @GetMapping(value="/invoices/{id}/document",produces="application/pdf") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')") ResponseEntity<byte[]> document(@PathVariable UUID id){var invoice=service.get(id);return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+invoice.invoiceNumber()+".pdf\"").body(documents.pdf(id));}
}
