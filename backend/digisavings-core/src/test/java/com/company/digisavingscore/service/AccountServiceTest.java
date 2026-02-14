package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.dto.response.UserProfileDetailsResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.AccountTransaction;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.entity.enums.TransactionType;
import com.company.digisavingscore.exception.AccountInactiveException;
import com.company.digisavingscore.exception.BusinessRuleViolationException;
import com.company.digisavingscore.exception.InsufficientBalanceException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.TransactionMapper;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.AccountTransactionRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private SavingsAccountRepository savingsAccountRepository;

    @Mock
    private AccountTransactionRepository accountTransactionRepository;

    @Mock
    private AccountApplicationRepository accountApplicationRepository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private AccountService accountService;

    private User testUser;
    private SavingsAccount testAccount;
    private AccountApplication testApplication;
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email(TEST_EMAIL)
                .password("hashedPassword")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        testAccount = SavingsAccount.builder()
                .id(1L)
                .accountNumber("ACC123456")
                .user(testUser)
                .balance(BigDecimal.valueOf(1000.00))
                .status(AccountStatus.ACTIVE)
                .interestRate(BigDecimal.valueOf(2.5))
                .openedDate(LocalDate.now())
                .build();

        testApplication = AccountApplication.builder()
                .id(1L)
                .user(testUser)
                .dob(LocalDate.of(1990, 1, 1))
                .occupation("Engineer")
                .incomeSource("Salary")
                .annualIncome(50000.0)
                .parentName("Parent Name")
                .applicationDate(LocalDateTime.now())
                .build();
    }

    // ===================== GET ACCOUNT DETAILS - HAPPY PATH =====================

    @Test
    @DisplayName("Happy: Should get account details successfully")
    void testGetAccountDetails_Success() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);
            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountApplicationRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(testApplication));

            AccountDetailsResponse response = accountService.getAccountDetails();

            assertThat(response).isNotNull();
            assertThat(response.getAccountId()).isEqualTo(1L);
            assertThat(response.getAccountNumber()).isEqualTo("ACC123456");
            assertThat(response.getAccountHolderName()).isEqualTo("Test User");
            assertThat(response.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
            assertThat(response.getStatus()).isEqualTo(AccountStatus.ACTIVE);

            verify(savingsAccountRepository).findByUserEmail(TEST_EMAIL);
            verify(accountApplicationRepository).findByUserId(testUser.getId());
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when account not found")
    void testGetAccountDetails_AccountNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);
            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.getAccountDetails())
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Account not found");

            verify(savingsAccountRepository).findByUserEmail(TEST_EMAIL);
            verifyNoInteractions(accountApplicationRepository);
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when application not found")
    void testGetAccountDetails_ApplicationNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);
            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountApplicationRepository.findByUserId(testUser.getId())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.getAccountDetails())
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Application not found");

            verify(savingsAccountRepository).findByUserEmail(TEST_EMAIL);
            verify(accountApplicationRepository).findByUserId(testUser.getId());
        }
    }

    // ===================== GET PROFILE DETAILS - HAPPY PATH =====================

    @Test
    @DisplayName("Happy: Should get profile details successfully")
    void testGetProfileDetails_Success() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);
            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountApplicationRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(testApplication));
            when(accountTransactionRepository.count()).thenReturn(5L);

            UserProfileDetailsResponse response = accountService.getProfileDetails();

            assertThat(response).isNotNull();
            assertThat(response.getAccountId()).isEqualTo(1L);
            assertThat(response.getOccupation()).isEqualTo("Engineer");
            assertThat(response.getIncomeSource()).isEqualTo("Salary");
            assertThat(response.getTransactionsCount()).isEqualTo(5L);

            verify(accountTransactionRepository).count();
        }
    }

    // ===================== DEPOSIT - HAPPY PATH =====================

    @Test
    @DisplayName("Happy: Should deposit successfully")
    void testDeposit_Success() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(500.00));
            request.setDescription("Test deposit");

            AccountTransaction savedTransaction = AccountTransaction.builder()
                    .id(1L)
                    .account(testAccount)
                    .transactionType(TransactionType.DEPOSIT)
                    .amount(BigDecimal.valueOf(500.00))
                    .transactionDate(LocalDateTime.now())
                    .description("Test deposit")
                    .build();

            TransactionResponse expectedResponse = TransactionResponse.builder()
                    .transactionId(1L)
                    .message("Deposit successful")
                    .build();

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class))).thenReturn(savedTransaction);
            when(mapper.toResponse(any(AccountTransaction.class), anyString())).thenReturn(expectedResponse);

            TransactionResponse response = accountService.deposit(request);

            assertThat(response).isNotNull();
            assertThat(testAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));

            verify(savingsAccountRepository).save(testAccount);
            verify(accountTransactionRepository).save(any(AccountTransaction.class));
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when deposit amount is zero")
    void testDeposit_ZeroAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.ZERO);
            request.setDescription("Invalid deposit");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.deposit(request))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("must be greater than zero");

            verify(savingsAccountRepository, never()).save(any());
            verify(accountTransactionRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when deposit amount is negative")
    void testDeposit_NegativeAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(-100.00));
            request.setDescription("Invalid deposit");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.deposit(request))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("must be greater than zero");
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when deposit amount is null")
    void testDeposit_NullAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(null);
            request.setDescription("Invalid deposit");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.deposit(request))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("must be greater than zero");
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when account is inactive for deposit")
    void testDeposit_InactiveAccount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            testAccount.setStatus(AccountStatus.CLOSED);
            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(500.00));
            request.setDescription("Test deposit");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.deposit(request))
                    .isInstanceOf(AccountInactiveException.class);

            verify(savingsAccountRepository, never()).save(any());
        }
    }

    // ===================== WITHDRAW - HAPPY PATH =====================

    @Test
    @DisplayName("Happy: Should withdraw successfully")
    void testWithdraw_Success() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(300.00));
            request.setDescription("Test withdrawal");

            AccountTransaction savedTransaction = AccountTransaction.builder()
                    .id(1L)
                    .account(testAccount)
                    .transactionType(TransactionType.WITHDRAW)
                    .amount(BigDecimal.valueOf(300.00))
                    .transactionDate(LocalDateTime.now())
                    .description("Test withdrawal")
                    .build();

            TransactionResponse expectedResponse = TransactionResponse.builder()
                    .transactionId(1L)
                    .message("Withdrawal successful")
                    .build();

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class))).thenReturn(savedTransaction);
            when(mapper.toResponse(any(AccountTransaction.class), anyString())).thenReturn(expectedResponse);

            TransactionResponse response = accountService.withdraw(request);

            assertThat(response).isNotNull();
            assertThat(testAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(700.00));

            verify(savingsAccountRepository).save(testAccount);
            verify(accountTransactionRepository).save(any(AccountTransaction.class));
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when insufficient balance")
    void testWithdraw_InsufficientBalance() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(2000.00));
            request.setDescription("Test withdrawal");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.withdraw(request))
                    .isInstanceOf(InsufficientBalanceException.class);

            verify(savingsAccountRepository, never()).save(any());
            verify(accountTransactionRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when withdraw amount is zero")
    void testWithdraw_ZeroAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.ZERO);
            request.setDescription("Invalid withdrawal");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.withdraw(request))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("must be greater than zero");
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when withdraw amount is negative")
    void testWithdraw_NegativeAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(-100.00));
            request.setDescription("Invalid withdrawal");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.withdraw(request))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("must be greater than zero");
        }
    }

    @Test
    @DisplayName("Edge: Should throw exception when account is inactive for withdrawal")
    void testWithdraw_InactiveAccount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            testAccount.setStatus(AccountStatus.CLOSED);
            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(100.00));
            request.setDescription("Test withdrawal");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.withdraw(request))
                    .isInstanceOf(AccountInactiveException.class);

            verify(savingsAccountRepository, never()).save(any());
        }
    }

    @Test
    @DisplayName("Edge: Should successfully withdraw exact balance amount")
    void testWithdraw_ExactBalance() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(1000.00));
            request.setDescription("Withdraw all");

            AccountTransaction savedTransaction = AccountTransaction.builder()
                    .id(1L)
                    .account(testAccount)
                    .transactionType(TransactionType.WITHDRAW)
                    .amount(BigDecimal.valueOf(1000.00))
                    .transactionDate(LocalDateTime.now())
                    .build();

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class))).thenReturn(savedTransaction);

            when(mapper.toResponse(any(AccountTransaction.class), anyString()))
                    .thenReturn(TransactionResponse.builder()
                            .transactionId(savedTransaction.getId())
                            .accountId(testAccount.getId())
                            .transactionType(savedTransaction.getTransactionType())
                            .amount(savedTransaction.getAmount())
                            .transactionDate(savedTransaction.getTransactionDate())
                            .description(request.getDescription())
                            .message("Withdrawal successful")
                            .build()
                    );

            TransactionResponse response = accountService.withdraw(request);

            assertThat(testAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(response.getMessage()).isEqualTo("Withdrawal successful");
            verify(savingsAccountRepository).save(testAccount);
        }
    }


    // ===================== GET TRANSACTION HISTORY - HAPPY PATH =====================

    @Test
    @DisplayName("Happy: Should get transaction history successfully")
    void testGetTransactionHistory_Success() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            AccountTransaction tx1 = AccountTransaction.builder()
                    .id(1L)
                    .account(testAccount)
                    .transactionType(TransactionType.DEPOSIT)
                    .amount(BigDecimal.valueOf(500.00))
                    .transactionDate(LocalDateTime.now())
                    .description("Initial deposit")
                    .build();

            AccountTransaction tx2 = AccountTransaction.builder()
                    .id(2L)
                    .account(testAccount)
                    .transactionType(TransactionType.WITHDRAW)
                    .amount(BigDecimal.valueOf(200.00))
                    .transactionDate(LocalDateTime.now())
                    .description("ATM withdrawal")
                    .build();

            List<AccountTransaction> transactions = Arrays.asList(tx1, tx2);

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.findByAccountIdOrderByTransactionDateDesc(anyLong()))
                    .thenReturn(transactions);

            when(mapper.toHistoryItems(anyList()))
                    .thenAnswer(invocation -> {
                        List<AccountTransaction> txList = invocation.getArgument(0);
                        return txList.stream()
                                .map(tx -> TransactionHistoryResponse.TransactionItem.builder()
                                        .transactionId(tx.getId())
                                        .transactionType(tx.getTransactionType())
                                        .amount(tx.getAmount())
                                        .transactionDate(tx.getTransactionDate())
                                        .description(tx.getDescription())
                                        .build())
                                .toList();
                    });

            when(mapper.toHistoryResponse(anyLong(), anyList()))
                    .thenAnswer(invocation -> {
                        List<TransactionHistoryResponse.TransactionItem> items = invocation.getArgument(1);
                        return TransactionHistoryResponse.builder()
                                .transactions(items)
                                .build();
                    });

            TransactionHistoryResponse response = accountService.getTransactionHistory();

            assertThat(response).isNotNull();
            assertThat(response.getTransactions()).hasSize(2);
            verify(accountTransactionRepository).findByAccountIdOrderByTransactionDateDesc(anyLong());
        }
    }


    @Test
    @DisplayName("Edge: Should return empty transaction history")
    void testGetTransactionHistory_Empty() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.findByAccountIdOrderByTransactionDateDesc(testAccount.getId()))
                    .thenReturn(Collections.emptyList());
            when(mapper.toHistoryResponse(anyLong(), anyList()))
                    .thenReturn(TransactionHistoryResponse.builder()
                            .transactions(Collections.emptyList())
                            .build());

            TransactionHistoryResponse response = accountService.getTransactionHistory();

            assertThat(response).isNotNull();
            assertThat(response.getTransactions()).isEmpty();
            verify(accountTransactionRepository).findByAccountIdOrderByTransactionDateDesc(testAccount.getId());
        }
    }


    @Test
    @DisplayName("Edge: Should throw exception when getting history for non-existent account")
    void testGetTransactionHistory_AccountNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);
            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.getTransactionHistory())
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Account not found");

            verify(accountTransactionRepository, never()).findByAccountIdOrderByTransactionDateDesc(anyLong());
        }
    }

    // ===================== EDGE CASES FOR MULTIPLE OPERATIONS =====================

    @Test
    @DisplayName("Edge: Should handle multiple deposits correctly")
    void testMultipleDeposits() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class)))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        tx.setId(1L);
                        return tx;
                    });
            when(mapper.toResponse(any(AccountTransaction.class), anyString()))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        return TransactionResponse.builder()
                                .transactionId(tx.getId())
                                .accountId(tx.getAccount().getId())
                                .transactionType(tx.getTransactionType())
                                .amount(tx.getAmount())
                                .transactionDate(tx.getTransactionDate())
                                .description(tx.getDescription())
                                .message("Deposit successful")
                                .build();
                    });

            TransactionRequest request1 = new TransactionRequest();
            request1.setAmount(BigDecimal.valueOf(100.00));

            TransactionRequest request2 = new TransactionRequest();
            request2.setAmount(BigDecimal.valueOf(200.00));

            accountService.deposit(request1);
            accountService.deposit(request2);

            assertThat(testAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1300.00));
            verify(accountTransactionRepository, times(2)).save(any(AccountTransaction.class));
        }
    }

    @Test
    @DisplayName("Edge: Should handle deposit and withdrawal combination")
    void testDepositAndWithdrawal() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class)))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        tx.setId(1L);
                        return tx;
                    });
            when(mapper.toResponse(any(AccountTransaction.class), anyString()))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        return TransactionResponse.builder()
                                .transactionId(tx.getId())
                                .accountId(tx.getAccount().getId())
                                .transactionType(tx.getTransactionType())
                                .amount(tx.getAmount())
                                .transactionDate(tx.getTransactionDate())
                                .description(tx.getDescription())
                                .message(tx.getTransactionType() == TransactionType.DEPOSIT ? "Deposit successful" : "Withdrawal successful")
                                .build();
                    });

            TransactionRequest depositRequest = new TransactionRequest();
            depositRequest.setAmount(BigDecimal.valueOf(500.00));

            TransactionRequest withdrawRequest = new TransactionRequest();
            withdrawRequest.setAmount(BigDecimal.valueOf(300.00));

            accountService.deposit(depositRequest);
            accountService.withdraw(withdrawRequest);

            assertThat(testAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1200.00));
            verify(accountTransactionRepository, times(2)).save(any(AccountTransaction.class));
        }
    }

    @Test
    @DisplayName("Edge: Should handle very large deposit amount")
    void testDeposit_VeryLargeAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(new BigDecimal("99999999999999999.99"));
            request.setDescription("Large deposit");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class)))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        tx.setId(1L);
                        return tx;
                    });
            when(mapper.toResponse(any(AccountTransaction.class), anyString()))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        return TransactionResponse.builder()
                                .transactionId(tx.getId())
                                .accountId(tx.getAccount().getId())
                                .transactionType(tx.getTransactionType())
                                .amount(tx.getAmount())
                                .transactionDate(tx.getTransactionDate())
                                .description(tx.getDescription())
                                .message("Deposit successful")
                                .build();
                    });

            accountService.deposit(request);

            verify(savingsAccountRepository).save(testAccount);
            verify(accountTransactionRepository).save(any(AccountTransaction.class));
        }
    }

    @Test
    @DisplayName("Edge: Should handle very small withdrawal amount")
    void testWithdraw_VerySmallAmount() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(TEST_EMAIL);

            TransactionRequest request = new TransactionRequest();
            request.setAmount(new BigDecimal("0.01"));
            request.setDescription("Small withdrawal");

            when(savingsAccountRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(testAccount));
            when(accountTransactionRepository.save(any(AccountTransaction.class)))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        tx.setId(1L);
                        return tx;
                    });
            when(mapper.toResponse(any(AccountTransaction.class), anyString()))
                    .thenAnswer(invocation -> {
                        AccountTransaction tx = invocation.getArgument(0);
                        return TransactionResponse.builder()
                                .transactionId(tx.getId())
                                .accountId(tx.getAccount().getId())
                                .transactionType(tx.getTransactionType())
                                .amount(tx.getAmount())
                                .transactionDate(tx.getTransactionDate())
                                .description(tx.getDescription())
                                .message("Withdrawal successful")
                                .build();
                    });

            accountService.withdraw(request);

            verify(savingsAccountRepository).save(testAccount);
            verify(accountTransactionRepository).save(any(AccountTransaction.class));
        }
    }

}