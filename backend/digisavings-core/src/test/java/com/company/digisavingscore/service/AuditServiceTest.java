package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.AuditEventResponse;
import com.company.digisavingscore.entity.*;
import com.company.digisavingscore.entity.enums.TransactionType;
import com.company.digisavingscore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Bharadi Harshitha
 */
@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AccountApplicationRepository applicationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @Mock
    private AccountTransactionRepository transactionRepository;

    @InjectMocks
    private AuditService auditService;

    private SavingsAccount testAccount;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().email("client@test.com").build();
        testAccount = SavingsAccount.builder()
                .accountNumber("SAV-12345")
                .user(testUser)
                .build();

        // Isolate transaction tests by stubbing other repos to return empty lists
        when(applicationRepository.findAllByOrderByReviewDateDesc()).thenReturn(Collections.emptyList());
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());
        when(savingsAccountRepository.findAllByOrderByOpenedDateDesc()).thenReturn(Collections.emptyList());
    }

    @Test
    @DisplayName("Should format action string correctly for DEPOSIT types")
    void should_FormatDepositAction_When_TransactionTypeIsDeposit() {
        // Arrange
        AccountTransaction depositTx = AccountTransaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("500.00"))
                .transactionDate(LocalDateTime.now())
                .account(testAccount)
                .description("Monthly savings")
                .build();

        when(transactionRepository.findAllByOrderByTransactionDateDesc()).thenReturn(List.of(depositTx));

        // Act
        List<AuditEventResponse> logs = auditService.getAuditLogs(null, null, null, null, 0, 10);

        // Assert
        assertEquals(1, logs.size());
        assertEquals("TRANSACTION_DEPOSIT", logs.get(0).getAction());
        assertEquals("INFO", logs.get(0).getSeverity());
    }

    @Test
    @DisplayName("Should assign WARNING severity and format action for large WITHDRAW transactions")
    void should_AssignWarningAndFormatWithdraw_When_WithdrawalIsHighValue() {
        // Arrange
        AccountTransaction largeWithdraw = AccountTransaction.builder()
                .transactionType(TransactionType.WITHDRAW)
                .amount(new BigDecimal("15000.00")) // Over the 10,000 threshold
                .transactionDate(LocalDateTime.now())
                .account(testAccount)
                .description("Large cash out")
                .build();

        when(transactionRepository.findAllByOrderByTransactionDateDesc()).thenReturn(List.of(largeWithdraw));

        // Act
        List<AuditEventResponse> logs = auditService.getAuditLogs(null, null, null, null, 0, 10);

        // Assert
        assertEquals("TRANSACTION_WITHDRAW", logs.get(0).getAction());
        assertEquals("WARNING", logs.get(0).getSeverity());
        assertTrue(logs.get(0).getDetails().contains("WITHDRAW: $15000.00"));
    }

    @Test
    @DisplayName("Should filter out transactions when eventType filter does not match")
    void should_ReturnEmptyList_When_EventTypeFilterDoesNotMatch() {
        // Arrange
        AccountTransaction tx = AccountTransaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .transactionDate(LocalDateTime.now())
                .account(testAccount)
                .amount(new BigDecimal("100"))
                .build();

        when(transactionRepository.findAllByOrderByTransactionDateDesc()).thenReturn(List.of(tx));

        // Act - Filtering for a type that doesn't exist in the data
        List<AuditEventResponse> logs = auditService.getAuditLogs(null, null, "TRANSACTION_TRANSFER", null, 0, 10);

        // Assert
        assertTrue(logs.isEmpty());
    }
}