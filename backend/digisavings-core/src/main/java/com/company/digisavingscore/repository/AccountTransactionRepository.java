package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    List<AccountTransaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);

    List<AccountTransaction> findByAccountId(Long accountId);

    List<AccountTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM AccountTransaction t ORDER BY t.transactionDate DESC")
    List<AccountTransaction> findAllByOrderByTransactionDateDesc();
}