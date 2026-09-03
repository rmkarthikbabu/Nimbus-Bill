package com.nimbusbill.customer.controller;
import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.service.AccessManagementService; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/access/users") @PreAuthorize("hasRole('ADMIN')") public class AccessManagementController{
 private final AccessManagementService service; public AccessManagementController(AccessManagementService s){service=s;}
 @GetMapping public List<AppUserResponse> list(){return service.list();} @PostMapping public AppUserResponse create(@Valid @RequestBody AppUserRequest r){return service.create(r);} @PutMapping("/{id}") public AppUserResponse update(@PathVariable UUID id,@Valid @RequestBody AppUserRequest r){return service.update(id,r);} @PostMapping("/{id}/status/{status}") public AppUserResponse status(@PathVariable UUID id,@PathVariable String status){return service.status(id,status.toUpperCase());}
}
