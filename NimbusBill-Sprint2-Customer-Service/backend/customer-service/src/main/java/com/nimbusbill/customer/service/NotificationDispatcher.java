package com.nimbusbill.customer.service;

import com.nimbusbill.customer.entity.NotificationOutbox;
import com.nimbusbill.customer.repository.NotificationOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;

@Service
public class NotificationDispatcher {
 private static final Logger log=LoggerFactory.getLogger(NotificationDispatcher.class);
 private static final int MAX_ATTEMPTS=5;
 private final NotificationOutboxRepository repository;
 public NotificationDispatcher(NotificationOutboxRepository repository){this.repository=repository;}

 @Scheduled(fixedDelayString="${app.notifications.poll-delay-ms:5000}")
 @Transactional
 public void dispatch(){
  Instant now=Instant.now();
  repository.ready(List.of("PENDING","FAILED"),now.plusMillis(1),org.springframework.data.domain.PageRequest.of(0,25)).forEach(this::deliver);
 }

 private void deliver(NotificationOutbox event){
  event.setStatus("PROCESSING");event.setAttemptCount(event.getAttemptCount()+1);repository.save(event);
  try{
   log.info("Notification delivered role={} destination={} subject={}",event.getRecipientRole(),event.getDestination(),event.getSubject());
   event.setStatus("SENT");event.setSentAt(Instant.now());event.setNextAttemptAt(null);event.setLastError(null);
  }catch(RuntimeException ex){
   event.setLastError(ex.getMessage());
   if(event.getAttemptCount()>=MAX_ATTEMPTS){event.setStatus("DEAD");event.setNextAttemptAt(null);}
   else{event.setStatus("FAILED");event.setNextAttemptAt(Instant.now().plusSeconds((long)Math.pow(2,event.getAttemptCount())*30));}
  }
  repository.save(event);
 }
}
