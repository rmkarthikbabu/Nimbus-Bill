package com.nimbusbill.customer.service;

import com.nimbusbill.customer.dto.CustomerPageResponse;
import com.nimbusbill.customer.dto.CustomerRequest;
import com.nimbusbill.customer.dto.CustomerResponse;
import com.nimbusbill.customer.dto.AuditLogResponse;
import com.nimbusbill.customer.entity.AuditLog;
import com.nimbusbill.customer.entity.Customer;
import com.nimbusbill.customer.entity.CustomerStatus;
import com.nimbusbill.customer.exception.ConflictException;
import com.nimbusbill.customer.exception.ResourceNotFoundException;
import com.nimbusbill.customer.mapper.CustomerMapper;
import com.nimbusbill.customer.repository.CustomerRepository;
import com.nimbusbill.customer.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;
import tools.jackson.databind.ObjectMapper;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final AuditLogRepository auditRepository;
    private final ObjectMapper objectMapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper, AuditLogRepository auditRepository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.auditRepository = auditRepository;
        this.objectMapper = objectMapper;
    }

    public CustomerResponse create(CustomerRequest request, String actor, String ipAddress) {
        if (repository.existsByCustomerCodeIgnoreCase(request.customerCode())) {
            throw new ConflictException("Customer code already exists: " + request.customerCode());
        }
        CustomerResponse created = mapper.toResponse(repository.save(mapper.toEntity(request)));
        audit(created.id(), "CREATED", null, json(created), actor, ipAddress);
        return created;
    }

    @Transactional(readOnly = true)
    public CustomerResponse get(UUID id) {
        return mapper.toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public CustomerPageResponse list(String search, CustomerStatus status, int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(sortDirection, allowedSort(sortBy)));
        boolean hasSearch = search != null && !search.isBlank();
        Page<Customer> result;
        if (hasSearch && status != null) result = repository.findByCustomerNameContainingIgnoreCaseAndStatus(search.trim(), status, pageable);
        else if (hasSearch) result = repository.findByCustomerNameContainingIgnoreCase(search.trim(), pageable);
        else if (status != null) result = repository.findByStatus(status, pageable);
        else result = repository.findAll(pageable);
        return new CustomerPageResponse(result.map(mapper::toResponse).getContent(), page, size, result.getTotalElements(), result.getTotalPages());
    }

    public CustomerResponse update(UUID id, CustomerRequest request, String actor, String ipAddress) {
        Customer customer = find(id);
        String oldValue = json(mapper.toResponse(customer));
        if (repository.existsByCustomerCodeIgnoreCaseAndIdNot(request.customerCode(), id)) {
            throw new ConflictException("Customer code already exists: " + request.customerCode());
        }
        mapper.update(customer, request);
        CustomerResponse updated = mapper.toResponse(repository.save(customer));
        audit(id, "UPDATED", oldValue, json(updated), actor, ipAddress);
        return updated;
    }

    public void delete(UUID id, String actor, String ipAddress) {
        Customer customer = find(id);
        String oldValue = json(mapper.toResponse(customer));
        customer.setStatus(CustomerStatus.INACTIVE);
        CustomerResponse updated = mapper.toResponse(repository.save(customer));
        audit(id, "SOFT_DELETED", oldValue, json(updated), actor, ipAddress);
    }

    public CustomerResponse changeStatus(UUID id, CustomerStatus status, String actor, String ipAddress) {
        Customer customer = find(id);
        String oldValue = json(mapper.toResponse(customer));
        customer.setStatus(status);
        CustomerResponse updated = mapper.toResponse(repository.save(customer));
        audit(id, status == CustomerStatus.ACTIVE ? "ACTIVATED" : status == CustomerStatus.SUSPENDED ? "SUSPENDED" : "STATUS_CHANGED", oldValue, json(updated), actor, ipAddress);
        return updated;
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> history(UUID id) {
        find(id);
        return auditRepository.findByCustomerIdOrderByCreatedAtDesc(id).stream()
                .map(a -> new AuditLogResponse(a.getId(), a.getCustomerId(), a.getAction(), a.getOldValue(), a.getNewValue(), a.getActor(), a.getIpAddress(), a.getCreatedAt())).toList();
    }

    private void audit(UUID customerId, String action, String oldValue, String newValue, String actor, String ipAddress) {
        AuditLog log = new AuditLog(); log.setCustomerId(customerId); log.setAction(action); log.setOldValue(oldValue); log.setNewValue(newValue);
        log.setActor(actor == null || actor.isBlank() ? "system" : actor); log.setIpAddress(ipAddress); auditRepository.save(log);
    }

    private String json(CustomerResponse response) { try { return objectMapper.writeValueAsString(response); } catch (Exception ex) { return "{}"; } }

    private Customer find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private String allowedSort(String sortBy) {
        return switch (sortBy == null ? "customerName" : sortBy) {
            case "customerCode", "customerName", "status", "country", "createdAt", "updatedAt" -> sortBy;
            default -> "customerName";
        };
    }
}
