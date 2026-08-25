package com.nimbusbill.customer.controller;

import com.nimbusbill.customer.dto.*;
import com.nimbusbill.customer.service.CustomerConfigurationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}")
public class CustomerConfigurationController {
 private final CustomerConfigurationService service;
 public CustomerConfigurationController(CustomerConfigurationService service){this.service=service;}
 @GetMapping("/products") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','OPERATIONS','AUDITOR','READ_ONLY')")
 List<CustomerProductResponse> products(@PathVariable UUID customerId){return service.products(customerId);}
 @PutMapping("/products") @PreAuthorize("hasAnyRole('ADMIN','OPERATIONS')")
 List<CustomerProductResponse> setProducts(@PathVariable UUID customerId,@Valid @RequestBody List<CustomerProductRequest> request){return service.setProducts(customerId,request);}
 @GetMapping("/pricing") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')")
 List<CustomerPricingResponse> pricing(@PathVariable UUID customerId){return service.pricing(customerId);}
 @PostMapping("/pricing") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
 CustomerPricingResponse assign(@PathVariable UUID customerId,@Valid @RequestBody CustomerPricingRequest request){return service.assign(customerId,request);}
 @PostMapping("/pricing/{assignmentId}/rollback") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
 CustomerPricingResponse rollback(@PathVariable UUID customerId,@PathVariable UUID assignmentId){return service.rollback(customerId,assignmentId);}
 @GetMapping("/pricing-overrides") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','AUDITOR','READ_ONLY')")
 List<PricingOverrideResponse> overrides(@PathVariable UUID customerId){return service.overrides(customerId);}
 @PostMapping("/pricing-overrides") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
 PricingOverrideResponse createOverride(@PathVariable UUID customerId,@Valid @RequestBody PricingOverrideRequest request,Principal principal,@RequestHeader(value="X-Actor",required=false) String actor){return service.createOverride(customerId,request,principal==null?(actor==null?"local-admin":actor):principal.getName());}
 @PutMapping("/pricing-overrides/{id}") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
 PricingOverrideResponse updateOverride(@PathVariable UUID customerId,@PathVariable UUID id,@Valid @RequestBody PricingOverrideRequest request){return service.updateOverride(customerId,id,request);}
 @DeleteMapping("/pricing-overrides/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
 void deactivateOverride(@PathVariable UUID customerId,@PathVariable UUID id){service.deactivateOverride(customerId,id);}
}
