package com.nimbusbill.customer.dto;
import java.util.List;
public record TransactionBatchResponse(int received,int completed,int rejected,int failed,List<String> errors,List<PaymentTransactionResponse> transactions){}
