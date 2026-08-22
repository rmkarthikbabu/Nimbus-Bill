package com.nimbusbill.customer.controller;
import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.entity.ProductStatus; import com.nimbusbill.customer.service.ProductService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.net.URI; import java.util.*;
@RestController @RequestMapping("/api/v1/products")
public class ProductController {
 private final ProductService service; public ProductController(ProductService service){this.service=service;}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest r){ProductResponse p=service.create(r);return ResponseEntity.created(URI.create("/api/v1/products/"+p.id())).body(p);}
 @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','OPERATIONS','AUDITOR','READ_ONLY')") List<ProductResponse> list(@RequestParam(required=false) ProductStatus status){return service.list(status);}
 @GetMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER','FINANCE','OPERATIONS','AUDITOR','READ_ONLY')") ProductResponse get(@PathVariable UUID id){return service.get(id);}
 @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','BILLING_MANAGER')") ProductResponse update(@PathVariable UUID id,@Valid @RequestBody ProductRequest r){return service.update(id,r);}
 @PostMapping("/{id}/activate") @PreAuthorize("hasRole('ADMIN')") ProductResponse activate(@PathVariable UUID id){return service.status(id,ProductStatus.ACTIVE);}
 @PostMapping("/{id}/deactivate") @PreAuthorize("hasRole('ADMIN')") ProductResponse deactivate(@PathVariable UUID id){return service.status(id,ProductStatus.INACTIVE);}
}
