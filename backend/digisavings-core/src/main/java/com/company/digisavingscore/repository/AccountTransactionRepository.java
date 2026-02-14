package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Khamitkar Sai Chandan
 */
@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    List<AccountTransaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);

    /**
     * @author Bharadi Harshitha
     */
    List<AccountTransaction> findByAccountId(Long accountId);

    /**
     * @author Bharadi Harshitha
     */
    List<AccountTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * @author Bharadi Harshitha
     */
    @Query("SELECT t FROM AccountTransaction t ORDER BY t.transactionDate DESC")
    List<AccountTransaction> findAllByOrderByTransactionDateDesc();
}