package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountTransaction;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.entity.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AccountTransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountTransactionRepository transactionRepository;

    private User testUser;
    private SavingsAccount testAccount;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("hashedPassword123")
                .role(Role.USER)
                .build();
        entityManager.persist(testUser);

        testAccount = SavingsAccount.builder()
                .accountNumber("ACC123456")
                .user(testUser)
                .balance(BigDecimal.valueOf(1000.00))
                .status(AccountStatus.ACTIVE)
                .interestRate(BigDecimal.valueOf(2.5))
                .openedDate(LocalDate.now())
                .build();
        entityManager.persist(testAccount);
        entityManager.flush();
    }

    // ===================== HAPPY PATH TESTS =====================

    @Test
    @DisplayName("Happy: Should save and retrieve transaction by account ID")
    void testFindByAccountId_Success() {
        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(500.00))
                .transactionDate(LocalDateTime.now())
                .description("Test deposit")
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(500.00));
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transactions.get(0).getDescription()).isEqualTo("Test deposit");
    }

    @Test
    @DisplayName("Happy: Should retrieve transactions ordered by date descending")
    void testFindByAccountIdOrderByTransactionDateDesc_Success() {
        AccountTransaction tx1 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now().minusDays(2))
                .description("Older transaction")
                .build();
        entityManager.persist(tx1);

        AccountTransaction tx2 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50.00))
                .transactionDate(LocalDateTime.now().minusDays(1))
                .description("Newer transaction")
                .build();
        entityManager.persist(tx2);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(testAccount.getId());

        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getDescription()).isEqualTo("Newer transaction");
        assertThat(transactions.get(1).getDescription()).isEqualTo("Older transaction");
        assertThat(transactions.get(0).getTransactionDate()).isAfter(transactions.get(1).getTransactionDate());
    }

    @Test
    @DisplayName("Happy: Should find transactions between date range")
    void testFindByTransactionDateBetween_Success() {
        LocalDateTime now = LocalDateTime.now();

        AccountTransaction tx1 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(now.minusDays(5))
                .build();
        entityManager.persist(tx1);

        AccountTransaction tx2 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(200.00))
                .transactionDate(now.minusDays(3))
                .build();
        entityManager.persist(tx2);

        AccountTransaction tx3 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50.00))
                .transactionDate(now.minusDays(1))
                .build();
        entityManager.persist(tx3);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByTransactionDateBetween(
                now.minusDays(4), now.minusDays(2));

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
    }

    @Test
    @DisplayName("Happy: Should find all transactions ordered by date descending")
    void testFindAllByOrderByTransactionDateDesc_Success() {
        User user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .password("password123")
                .role(Role.USER)
                .build();
        entityManager.persist(user2);

        SavingsAccount account2 = SavingsAccount.builder()
                .accountNumber("ACC789012")
                .user(user2)
                .balance(BigDecimal.valueOf(2000.00))
                .status(AccountStatus.ACTIVE)
                .interestRate(BigDecimal.valueOf(3.0))
                .openedDate(LocalDate.now())
                .build();
        entityManager.persist(account2);

        AccountTransaction tx1 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now().minusDays(3))
                .build();
        entityManager.persist(tx1);

        AccountTransaction tx2 = AccountTransaction.builder()
                .account(account2)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50.00))
                .transactionDate(LocalDateTime.now().minusDays(1))
                .build();
        entityManager.persist(tx2);

        AccountTransaction tx3 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(200.00))
                .transactionDate(LocalDateTime.now().minusDays(2))
                .build();
        entityManager.persist(tx3);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findAllByOrderByTransactionDateDesc();

        assertThat(transactions).hasSize(3);
        assertThat(transactions.get(0).getTransactionDate()).isAfter(transactions.get(1).getTransactionDate());
        assertThat(transactions.get(1).getTransactionDate()).isAfter(transactions.get(2).getTransactionDate());
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
    }

    @Test
    @DisplayName("Happy: Should handle multiple transactions for same account")
    void testMultipleTransactionsForSameAccount() {
        AccountTransaction tx1 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now().minusHours(2))
                .description("First deposit")
                .build();
        entityManager.persist(tx1);

        AccountTransaction tx2 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(30.00))
                .transactionDate(LocalDateTime.now().minusHours(1))
                .description("First withdrawal")
                .build();
        entityManager.persist(tx2);

        AccountTransaction tx3 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(50.00))
                .transactionDate(LocalDateTime.now())
                .description("Second deposit")
                .build();
        entityManager.persist(tx3);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(3);
        assertThat(transactions).extracting(AccountTransaction::getAmount)
                .containsExactlyInAnyOrder(
                        BigDecimal.valueOf(100.00),
                        BigDecimal.valueOf(30.00),
                        BigDecimal.valueOf(50.00)
                );
    }

    @Test
    @DisplayName("Happy: Should handle different transaction types")
    void testDifferentTransactionTypes() {
        AccountTransaction deposit = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(500.00))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(deposit);

        AccountTransaction withdrawal = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(withdrawal);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting(AccountTransaction::getTransactionType)
                .containsExactlyInAnyOrder(TransactionType.DEPOSIT, TransactionType.WITHDRAW);
    }

    // ===================== EDGE CASES & FAILURE TESTS =====================

    @Test
    @DisplayName("Edge: Should return empty list when no transactions exist for account")
    void testFindByAccountId_Empty() {
        List<AccountTransaction> transactions = transactionRepository.findByAccountId(9999L);

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Edge: Should return empty list when account ID is null")
    void testFindByAccountId_NullId() {
        List<AccountTransaction> transactions = transactionRepository.findByAccountId(null);

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Edge: Should return empty list for null account ID when ordering by date")
    void testFindByAccountIdOrderByTransactionDateDesc_NullId() {
        List<AccountTransaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(null);

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Edge: Should return empty list when no transactions in date range")
    void testFindByTransactionDateBetween_Empty() {
        LocalDateTime now = LocalDateTime.now();

        List<AccountTransaction> transactions = transactionRepository.findByTransactionDateBetween(
                now.minusDays(10), now.minusDays(8));

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Edge: Should return empty list when no transactions exist globally")
    void testFindAllByOrderByTransactionDateDesc_Empty() {
        List<AccountTransaction> transactions = transactionRepository.findAllByOrderByTransactionDateDesc();

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Edge: Should handle transactions with null descriptions")
    void testTransactionWithNullDescription() {
        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(500.00))
                .transactionDate(LocalDateTime.now())
                .description(null)
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getDescription()).isNull();
    }

    @Test
    @DisplayName("Edge: Should handle transactions with very large amounts")
    void testTransactionWithLargeAmount() {
        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("99999999999999999.99"))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("99999999999999999.99"));
    }

    @Test
    @DisplayName("Edge: Should handle transactions with very small amounts")
    void testTransactionWithSmallAmount() {
        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(new BigDecimal("0.01"))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    @DisplayName("Edge: Should handle transactions with same timestamp")
    void testTransactionsWithSameTimestamp() {
        LocalDateTime sameTime = LocalDateTime.now();

        AccountTransaction tx1 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(sameTime)
                .description("First")
                .build();
        entityManager.persist(tx1);

        AccountTransaction tx2 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50.00))
                .transactionDate(sameTime)
                .description("Second")
                .build();
        entityManager.persist(tx2);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(2);
    }

    @Test
    @DisplayName("Edge: Should handle boundary dates for date range query")
    void testFindByTransactionDateBetween_BoundaryDates() {
        LocalDateTime exactTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(exactTime)
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByTransactionDateBetween(
                exactTime, exactTime.plusMinutes(1));

        assertThat(transactions).hasSize(1);
    }

    @Test
    @DisplayName("Edge: Should exclude transactions outside date range boundaries")
    void testFindByTransactionDateBetween_ExcludeBoundaries() {
        LocalDateTime now = LocalDateTime.now();

        AccountTransaction beforeRange = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(now.minusDays(10))
                .build();
        entityManager.persist(beforeRange);

        AccountTransaction inRange = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(200.00))
                .transactionDate(now.minusDays(5))
                .build();
        entityManager.persist(inRange);

        AccountTransaction afterRange = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50.00))
                .transactionDate(now.minusDays(1))
                .build();
        entityManager.persist(afterRange);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByTransactionDateBetween(
                now.minusDays(6), now.minusDays(4));

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
    }

    @Test
    @DisplayName("Edge: Should handle description strings up to 255 characters")
    void testTransactionWithLongDescription() {
        String longDescription = "A".repeat(255);

        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .description(longDescription)
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountId(testAccount.getId());

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getDescription()).hasSize(255);
    }

    @Test
    @DisplayName("Edge: Should throw exception when description exceeds 255 characters")
    void testTransactionWithTooLongDescription() {
        String tooLongDescription = "A".repeat(256);

        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .description(tooLongDescription)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(transaction);
            entityManager.flush();
        }).isInstanceOf(Exception.class)
                .hasMessageContaining("too long");
    }

    @Test
    @DisplayName("Edge: Should correctly order when only one transaction exists")
    void testFindByAccountIdOrderByTransactionDateDesc_SingleTransaction() {
        AccountTransaction transaction = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(transaction);
        entityManager.flush();

        List<AccountTransaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(testAccount.getId());

        assertThat(transactions).hasSize(1);
    }

    @Test
    @DisplayName("Edge: Should handle multiple accounts with no cross-contamination")
    void testMultipleAccounts_NoDataLeakage() {
        User user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .password("password123")
                .role(Role.USER)
                .build();
        entityManager.persist(user2);

        SavingsAccount account2 = SavingsAccount.builder()
                .accountNumber("ACC999999")
                .user(user2)
                .balance(BigDecimal.valueOf(5000.00))
                .status(AccountStatus.ACTIVE)
                .interestRate(BigDecimal.valueOf(3.5))
                .openedDate(LocalDate.now())
                .build();
        entityManager.persist(account2);

        AccountTransaction tx1 = AccountTransaction.builder()
                .account(testAccount)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(tx1);

        AccountTransaction tx2 = AccountTransaction.builder()
                .account(account2)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(200.00))
                .transactionDate(LocalDateTime.now())
                .build();
        entityManager.persist(tx2);
        entityManager.flush();

        List<AccountTransaction> account1Transactions = transactionRepository.findByAccountId(testAccount.getId());
        List<AccountTransaction> account2Transactions = transactionRepository.findByAccountId(account2.getId());

        assertThat(account1Transactions).hasSize(1);
        assertThat(account2Transactions).hasSize(1);
        assertThat(account1Transactions.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(account2Transactions.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
    }
}