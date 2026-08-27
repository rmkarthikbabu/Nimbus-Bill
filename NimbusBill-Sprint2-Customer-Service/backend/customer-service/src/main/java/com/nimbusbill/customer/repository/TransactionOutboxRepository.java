package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.TransactionOutbox; import org.springframework.data.domain.Pageable; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.time.Instant; import java.util.*;
public interface TransactionOutboxRepository extends JpaRepository<TransactionOutbox,UUID>{
 @Query("select e from TransactionOutbox e where e.status in :statuses and (e.nextAttemptAt is null or e.nextAttemptAt<=:now) order by e.createdAt") List<TransactionOutbox> ready(@Param("statuses") List<String> statuses,@Param("now") Instant now,Pageable page);
 long countByStatus(String status); List<TransactionOutbox> findTop100ByOrderByCreatedAtDesc();
}
