package com.nimbusbill.customer.dto; import com.nimbusbill.customer.entity.BillingCycle; import java.time.Instant; import java.util.UUID;
public record BillingAccountResponse(UUID id,UUID customerId,String accountCode,String accountName,String currency,BillingCycle billingCycle,int paymentTermsDays,String status,Instant createdAt){}
