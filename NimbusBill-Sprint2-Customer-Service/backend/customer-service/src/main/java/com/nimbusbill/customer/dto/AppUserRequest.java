package com.nimbusbill.customer.dto;
import jakarta.validation.constraints.*; import java.util.*;
public record AppUserRequest(@NotBlank @Email String email,@NotBlank String displayName,@NotBlank String role,boolean mfaRequired,Set<UUID> customerIds){}
