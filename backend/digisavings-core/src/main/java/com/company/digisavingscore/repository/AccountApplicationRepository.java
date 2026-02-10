package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
    List<AccountApplication> findByStatus(ApplicationStatus status);
}