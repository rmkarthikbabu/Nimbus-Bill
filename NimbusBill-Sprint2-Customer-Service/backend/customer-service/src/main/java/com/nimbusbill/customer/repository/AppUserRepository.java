package com.nimbusbill.customer.repository;
import com.nimbusbill.customer.entity.AppUser; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface AppUserRepository extends JpaRepository<AppUser,UUID>{boolean existsByEmailIgnoreCase(String email); Optional<AppUser> findByEmailIgnoreCase(String email); List<AppUser> findAllByOrderByDisplayNameAsc();}
