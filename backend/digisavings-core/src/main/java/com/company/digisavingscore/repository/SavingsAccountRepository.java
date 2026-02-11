package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    Optional<SavingsAccount> findByAccountNumber(String accountNumber);

    boolean existsByUser(User user);

    Optional<SavingsAccount> findByUser(User user);

    @Query("SELECT s FROM SavingsAccount s WHERE s.user.email = :email")
    Optional<SavingsAccount> findByUserEmail(String email);

    @Query("SELECT COALESCE(SUM(s.balance), 0) FROM SavingsAccount s WHERE s.status = 'ACTIVE'")
    BigDecimal getTotalBalance();

    @Query("SELECT COALESCE(SUM(s.balance), 0) FROM SavingsAccount s WHERE s.status = 'ACTIVE' " +
            "AND s.openedDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalBalanceForPeriod(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(AVG(s.interestRate), 0) FROM SavingsAccount s WHERE s.status = 'ACTIVE'")
    Double getAverageInterestRate();

    Long countByStatus(AccountStatus status);

    List<SavingsAccount> findByOpenedDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT s FROM SavingsAccount s ORDER BY s.openedDate DESC")
    List<SavingsAccount> findAllByOrderByOpenedDateDesc();
}