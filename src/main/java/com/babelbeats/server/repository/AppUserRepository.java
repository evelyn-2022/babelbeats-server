package com.babelbeats.server.repository;
import java.util.Optional;

import com.babelbeats.server.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByProviderId(String providerId);
}