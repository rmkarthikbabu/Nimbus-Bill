package com.nimbusbill.customer.controller;

import com.nimbusbill.customer.dto.*; import com.nimbusbill.customer.entity.PaymentTransactionStatus; import com.nimbusbill.customer.service.PaymentTransactionService;
import org.junit.jupiter.api.Test; import org.springframework.http.HttpStatus; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;

class PaymentTransactionControllerTest {
 private final PaymentTransactionService service=mock(PaymentTransactionService.class); private final PaymentTransactionController controller=new PaymentTransactionController(service);
 @Test void completedTransactionReturnsCreated(){UUID id=UUID.randomUUID();var result=response(id,PaymentTransactionStatus.COMPLETED,null);when(service.ingest(any())).thenReturn(result);var request=new PaymentTransactionRequest(UUID.randomUUID(),"REF-1","UPI","UPI",BigDecimal.TEN,"INR","X","Y");var http=controller.ingest(request);assertEquals(HttpStatus.CREATED,http.getStatusCode());assertEquals(id,http.getBody().id());assertEquals("/api/v1/transactions/"+id,http.getHeaders().getLocation().getPath());}
 @Test void rejectedTransactionReturnsUnprocessableAndKeepsIdentity(){UUID id=UUID.randomUUID();var result=response(id,PaymentTransactionStatus.REJECTED,"Transaction limit exceeded");when(service.ingest(any())).thenReturn(result);var request=new PaymentTransactionRequest(UUID.randomUUID(),"REF-2","UPI","UPI",BigDecimal.TEN,"INR",null,null);var http=controller.ingest(request);assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,http.getStatusCode());assertEquals("Transaction limit exceeded",http.getBody().failureReason());}
 private PaymentTransactionResponse response(UUID id,PaymentTransactionStatus status,String reason){return new PaymentTransactionResponse(id,UUID.randomUUID(),"REF","UPI","UPI",BigDecimal.TEN,"INR","X","Y",status,reason,BigDecimal.ONE,BigDecimal.ZERO,BigDecimal.ONE,UUID.randomUUID(),"PENDING","PAYMENT","REST",null,"PENDING",null,Instant.now(),status==PaymentTransactionStatus.COMPLETED?Instant.now():null,List.of(new PaymentTransactionResponse.StatusEntry(status,reason,Instant.now())));}
}
