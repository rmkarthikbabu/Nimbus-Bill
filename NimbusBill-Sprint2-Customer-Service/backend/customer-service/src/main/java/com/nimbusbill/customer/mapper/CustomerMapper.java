package com.nimbusbill.customer.mapper;

import com.nimbusbill.customer.dto.CustomerRequest;
import com.nimbusbill.customer.dto.CustomerResponse;
import com.nimbusbill.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        update(customer, request);
        return customer;
    }

    public void update(Customer customer, CustomerRequest request) {
        customer.setCustomerCode(request.customerCode().trim().toUpperCase());
        customer.setCustomerName(request.customerName().trim());
        customer.setLegalName(request.legalName().trim());
        customer.setCustomerType(request.customerType().trim());
        customer.setIndustry(trimToNull(request.industry()));
        customer.setCountry(request.country().trim().toUpperCase());
        customer.setCurrency(request.currency().trim().toUpperCase());
        customer.setBillingCycle(request.billingCycle());
        customer.setStatus(request.status());
        customer.setTaxIdentifier(trimToNull(request.taxIdentifier()));
        customer.setWebsite(trimToNull(request.website()));
    }

    public CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getCustomerCode(), c.getCustomerName(), c.getLegalName(),
                c.getCustomerType(), c.getIndustry(), c.getCountry(), c.getCurrency(), c.getBillingCycle(),
                c.getStatus(), c.getTaxIdentifier(), c.getWebsite(), c.getCreatedAt(), c.getUpdatedAt(), c.getVersion());
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }
}
