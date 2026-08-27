package com.nimbusbill.customer.controller;
import com.nimbusbill.customer.dto.TransactionOutboxResponse; import com.nimbusbill.customer.repository.TransactionOutboxRepository; import com.nimbusbill.customer.service.TransactionOutboxDispatcher; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/transaction-outbox") public class TransactionOutboxController {
 private final TransactionOutboxRepository repository; private final TransactionOutboxDispatcher dispatcher; public TransactionOutboxController(TransactionOutboxRepository r,TransactionOutboxDispatcher d){repository=r;dispatcher=d;}
 @GetMapping @PreAuthorize("hasAnyRole('ADMIN','OPERATIONS','AUDITOR')") List<TransactionOutboxResponse> list(){return repository.findTop100ByOrderByCreatedAtDesc().stream().map(e->new TransactionOutboxResponse(e.getId(),e.getTransaction().getId(),e.getEventType(),e.getStatus(),e.getAttemptCount(),e.getLastError(),e.getNextAttemptAt(),e.getCreatedAt(),e.getPublishedAt())).toList();}
 @PostMapping("/{id}/retry") @PreAuthorize("hasAnyRole('ADMIN','OPERATIONS')") void retry(@PathVariable UUID id){dispatcher.retry(id);}
}
