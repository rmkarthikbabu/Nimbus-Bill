package com.nimbusbill.customer.controller;

import com.nimbusbill.customer.dto.CustomerRequest;
import com.nimbusbill.customer.entity.BillingCycle;
import com.nimbusbill.customer.entity.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test void customerLifecycleAndAudit() throws Exception {
        String code = "LIFE_" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        String body = mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(json(request(code, "Lifecycle Customer"))))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.customerCode").value(code)).andReturn().getResponse().getContentAsString();
        String id = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(get("/api/v1/customers/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.customerName").value("Lifecycle Customer"));
        mockMvc.perform(put("/api/v1/customers/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json(request(code, "Updated Customer"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.customerName").value("Updated Customer"));
        mockMvc.perform(post("/api/v1/customers/{id}/suspend", id)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("SUSPENDED"));
        mockMvc.perform(post("/api/v1/customers/{id}/activate", id)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACTIVE"));
        mockMvc.perform(get("/api/v1/customers/{id}/history", id)).andExpect(status().isOk()).andExpect(jsonPath("$[0].action").value("ACTIVATED")).andExpect(jsonPath("$.length()").value(4));
        mockMvc.perform(delete("/api/v1/customers/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/customers/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test void listSupportsSearchAndStatusFilter() throws Exception {
        String code="SEARCH_"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(json(request(code,"Unique Search Name")))).andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/customers").param("search","Unique Search").param("status","PENDING"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1)).andExpect(jsonPath("$.content[0].customerCode").value(code));
    }

    @Test void rejectsValidationAndDuplicateCodes() throws Exception {
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Validation failed"));
        String code="DUP_"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
        String payload=json(request(code,"First"));
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isConflict());
    }

    @Test void returnsNotFoundForUnknownCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/customers/{id}",UUID.randomUUID())).andExpect(status().isNotFound());
    }

    private CustomerRequest request(String code,String name){return new CustomerRequest(code,name,name+" Ltd","CORPORATE","Technology","IN","INR",BillingCycle.MONTHLY,CustomerStatus.PENDING,"29ABCDE1234F1Z5","https://test.example");}
    private String json(Object value) throws Exception{return objectMapper.writeValueAsString(value);}
}
