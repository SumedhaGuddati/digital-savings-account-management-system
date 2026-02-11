package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    List<SavingsAccount> findByStatus(AccountStatus status);

    Optional<SavingsAccount> findByUser(User user);

    boolean existsByUser(User user);

    Optional<SavingsAccount> findByUserEmail(String email);
=======

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    boolean existsByUser(User user);
>>>>>>> ca268d4 (feat(account): enhance account APIs and repository logic)
}