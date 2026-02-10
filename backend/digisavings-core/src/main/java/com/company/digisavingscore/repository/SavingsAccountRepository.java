package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    List<SavingsAccount> findByStatus(AccountStatus status);

    Optional<SavingsAccount> findByUser(User user);

    boolean existsByUser(User user);

    Optional<SavingsAccount> findByUserEmail(String email);
}