package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
    // Used to check if user already has an application
    Optional<AccountApplication> findByUserId(Long userId);
}