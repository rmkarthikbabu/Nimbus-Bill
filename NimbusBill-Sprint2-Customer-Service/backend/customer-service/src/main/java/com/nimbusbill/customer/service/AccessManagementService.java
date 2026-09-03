package com.nimbusbill.customer.service;
import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.entity.AppUser; import com.nimbusbill.customer.exception.*; import com.nimbusbill.customer.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service @Transactional public class AccessManagementService{
 private static final Set<String> ROLES=Set.of("ADMIN","BILLING_MANAGER","FINANCE","OPERATIONS","AUDITOR","READ_ONLY","AI_ANALYST"); private final AppUserRepository users; private final CustomerRepository customers;
 public AccessManagementService(AppUserRepository u,CustomerRepository c){users=u;customers=c;}
 @Transactional(readOnly=true) public List<AppUserResponse> list(){return users.findAllByOrderByDisplayNameAsc().stream().map(this::response).toList();}
 public AppUserResponse create(AppUserRequest r){if(users.existsByEmailIgnoreCase(r.email()))throw new ConflictException("User email already exists");AppUser u=new AppUser();apply(u,r);return response(users.save(u));}
 public AppUserResponse update(UUID id,AppUserRequest r){AppUser u=find(id);apply(u,r);return response(users.save(u));}
 public AppUserResponse status(UUID id,String status){if(!Set.of("ACTIVE","SUSPENDED","INACTIVE").contains(status))throw new IllegalArgumentException("Invalid user status");AppUser u=find(id);u.setStatus(status);return response(users.save(u));}
 private void apply(AppUser u,AppUserRequest r){String role=r.role().toUpperCase();if(!ROLES.contains(role))throw new IllegalArgumentException("Invalid role");Set<UUID> scope=r.customerIds()==null?Set.of():r.customerIds();scope.forEach(id->{if(!customers.existsById(id))throw new ResourceNotFoundException("Customer not found: "+id);});u.setEmail(r.email().trim().toLowerCase());u.setDisplayName(r.displayName().trim());u.setRole(role);u.setMfaRequired(r.mfaRequired());u.getCustomerIds().clear();u.getCustomerIds().addAll(scope);}
 private AppUser find(UUID id){return users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found: "+id));} private AppUserResponse response(AppUser u){return new AppUserResponse(u.getId(),u.getEmail(),u.getDisplayName(),u.getRole(),u.getStatus(),u.isMfaRequired(),Set.copyOf(u.getCustomerIds()),u.getCreatedAt(),u.getUpdatedAt());}
}
