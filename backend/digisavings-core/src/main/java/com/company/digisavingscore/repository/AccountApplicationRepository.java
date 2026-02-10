package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
    Optional<AccountApplication> findByUserId(Long userId);

    List<AccountApplication> findByStatus(ApplicationStatus status);
}