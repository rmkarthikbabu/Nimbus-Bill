package com.nimbusbill.customer.dto;
import java.time.Instant; import java.util.*;
public record AppUserResponse(UUID id,String email,String displayName,String role,String status,boolean mfaRequired,Set<UUID> customerIds,Instant createdAt,Instant updatedAt){}
