package com.nimbusbill.customer.controller;

import com.nimbusbill.customer.dto.CustomerPageResponse;
import com.nimbusbill.customer.dto.CustomerRequest;
import com.nimbusbill.customer.dto.CustomerResponse;
import com.nimbusbill.customer.dto.AuditLogResponse;
import com.nimbusbill.customer.entity.CustomerStatus;
import com.nimbusbill.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;
import java.util.List;
import java.security.Principal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) { this.service = service; }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
    ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request, Principal principal, HttpServletRequest http) {
        CustomerResponse created = service.create(request, actor(principal), http.getRemoteAddr());
        return ResponseEntity.created(URI.create("/api/v1/customers/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','OPERATIONS','AUDITOR','READ_ONLY')")
    CustomerResponse get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','OPERATIONS','AUDITOR','READ_ONLY')")
    CustomerPageResponse list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) CustomerStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "customerName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return service.list(search, status, page, size, sortBy, direction);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')")
    CustomerResponse update(@PathVariable UUID id, @Valid @RequestBody CustomerRequest request, Principal principal, HttpServletRequest http) {
        return service.update(id, request, actor(principal), http.getRemoteAddr());
    }

    @PostMapping("/{id}/activate") @PreAuthorize("hasAnyRole('ADMIN','OPERATIONS')")
    CustomerResponse activate(@PathVariable UUID id, Principal principal, HttpServletRequest http){return service.changeStatus(id, CustomerStatus.ACTIVE, actor(principal), http.getRemoteAddr());}

    @PostMapping("/{id}/suspend") @PreAuthorize("hasAnyRole('ADMIN','OPERATIONS')")
    CustomerResponse suspend(@PathVariable UUID id, Principal principal, HttpServletRequest http){return service.changeStatus(id, CustomerStatus.SUSPENDED, actor(principal), http.getRemoteAddr());}

    @GetMapping("/{id}/history") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','AUDITOR')")
    List<AuditLogResponse> history(@PathVariable UUID id){return service.history(id);}

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id, Principal principal, HttpServletRequest http) { service.delete(id, actor(principal), http.getRemoteAddr()); }

    private String actor(Principal principal){return principal == null ? "local-admin" : principal.getName();}
}
