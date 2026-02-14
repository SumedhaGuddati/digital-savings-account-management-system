package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.dto.response.UserProfileDetailsResponse;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.AccountType;
import com.company.digisavingscore.entity.enums.TransactionType;
import com.company.digisavingscore.exception.AccountInactiveException;
import com.company.digisavingscore.exception.BusinessRuleViolationException;
import com.company.digisavingscore.exception.InsufficientBalanceException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.service.IAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAccountService accountService;

    private AccountDetailsResponse accountDetailsResponse;
    private UserProfileDetailsResponse profileDetailsResponse;
    private TransactionResponse transactionResponse;
    private TransactionHistoryResponse transactionHistoryResponse;
    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        accountDetailsResponse = AccountDetailsResponse.builder()
                .accountId(1L)
                .accountNumber("ACC123456")
                .accountHolderName("Test User")
                .balance(BigDecimal.valueOf(1000.00))
                .status(AccountStatus.ACTIVE)
                .accountType(AccountType.ZETA_ULTRA)
                .build();

        profileDetailsResponse = UserProfileDetailsResponse.builder()
                .accountId(1L)
                .accountNumber("ACC123456")
                .accountHolderName("Test User")
                .balance(BigDecimal.valueOf(1000.00))
                .status(AccountStatus.ACTIVE)
                .accountType(AccountType.ZETA_PRIME)
                .dob(LocalDate.of(1990, 1, 1))
                .applicationDate(LocalDateTime.now())
                .occupation("Engineer")
                .incomeSource("Salary")
                .annualIncome(50000.0)
                .parentName("Parent Name")
                .transactionsCount(5L)
                .build();

        transactionResponse = TransactionResponse.builder()
                .transactionId(1L)
                .accountId(1L)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(500.00))
                .transactionDate(LocalDateTime.now())
                .description("Test deposit")
                .message("Deposit successful")
                .build();

        TransactionHistoryResponse.TransactionItem item1 = TransactionHistoryResponse.TransactionItem.builder()
                .transactionId(1L)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(500.00))
                .transactionDate(LocalDateTime.now())
                .description("Deposit")
                .build();

        TransactionHistoryResponse.TransactionItem item2 = TransactionHistoryResponse.TransactionItem.builder()
                .transactionId(2L)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(200.00))
                .transactionDate(LocalDateTime.now())
                .description("Withdrawal")
                .build();

        transactionHistoryResponse = TransactionHistoryResponse.builder()
                .accountId(1L)
                .transactions(Arrays.asList(item1, item2))
                .build();

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(BigDecimal.valueOf(500.00));
        transactionRequest.setDescription("Test transaction");
    }

    // ===================== GET MY ACCOUNT - HAPPY PATH =====================

    @Test
    @WithMockUser
    @DisplayName("Happy: Should get account details successfully")
    void testGetMyAccount_Success() throws Exception {
        when(accountService.getAccountDetails()).thenReturn(accountDetailsResponse);

        mockMvc.perform(get("/api/accounts/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.accountHolderName").value("Test User"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.accountType").value(AccountType.ZETA_ULTRA.name()));

        verify(accountService, times(1)).getAccountDetails();
    }

    @Test
    @DisplayName("Edge: Should return 401 when user not authenticated")
    void testGetMyAccount_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/accounts/me"))
                .andExpect(status().isUnauthorized());

        verify(accountService, never()).getAccountDetails();
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 404 when account not found")
    void testGetMyAccount_NotFound() throws Exception {
        when(accountService.getAccountDetails())
                .thenThrow(new ResourceNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/me"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getAccountDetails();
    }

    // ===================== GET MY PROFILE - HAPPY PATH =====================

    @Test
    @WithMockUser
    @DisplayName("Happy: Should get profile details successfully")
    void testGetMyProfile_Success() throws Exception {
        when(accountService.getProfileDetails()).thenReturn(profileDetailsResponse);

        mockMvc.perform(get("/api/accounts/me/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.occupation").value("Engineer"))
                .andExpect(jsonPath("$.incomeSource").value("Salary"))
                .andExpect(jsonPath("$.annualIncome").value(50000.0))
                .andExpect(jsonPath("$.transactionsCount").value(5L));

        verify(accountService, times(1)).getProfileDetails();
    }

    @Test
    @DisplayName("Edge: Should return 401 when getting profile without authentication")
    void testGetMyProfile_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/accounts/me/profile"))
                .andExpect(status().isUnauthorized());

        verify(accountService, never()).getProfileDetails();
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 404 when profile not found")
    void testGetMyProfile_NotFound() throws Exception {
        when(accountService.getProfileDetails())
                .thenThrow(new ResourceNotFoundException("Profile not found"));

        mockMvc.perform(get("/api/accounts/me/profile"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getProfileDetails();
    }

    // ===================== DEPOSIT - HAPPY PATH =====================

    @Test
    @WithMockUser
    @DisplayName("Happy: Should deposit successfully")
    void testDeposit_Success() throws Exception {
        when(accountService.deposit(any(TransactionRequest.class))).thenReturn(transactionResponse);

        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(500.00))
                .andExpect(jsonPath("$.message").value("Deposit successful"));

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Edge: Should return 401 when depositing without authentication")
    void testDeposit_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isUnauthorized());

        verify(accountService, never()).deposit(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 400 when deposit amount is invalid")
    void testDeposit_InvalidAmount() throws Exception {
        when(accountService.deposit(any(TransactionRequest.class)))
                .thenThrow(new BusinessRuleViolationException("Amount must be greater than zero"));

        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 400 when account is inactive for deposit")
    void testDeposit_InactiveAccount() throws Exception {
        when(accountService.deposit(any(TransactionRequest.class)))
                .thenThrow(new AccountInactiveException());

        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 400 when deposit request body is empty")
    void testDeposit_EmptyBody() throws Exception {
        mockMvc.perform(post("/api/accounts/me/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===================== WITHDRAW - HAPPY PATH =====================

    @Test
    @WithMockUser
    @DisplayName("Happy: Should withdraw successfully")
    void testWithdraw_Success() throws Exception {
        TransactionResponse withdrawResponse = TransactionResponse.builder()
                .transactionId(2L)
                .accountId(1L)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(300.00))
                .transactionDate(LocalDateTime.now())
                .description("Test withdrawal")
                .message("Withdrawal successful")
                .build();

        when(accountService.withdraw(any(TransactionRequest.class))).thenReturn(withdrawResponse);

        TransactionRequest withdrawRequest = new TransactionRequest();
        withdrawRequest.setAmount(BigDecimal.valueOf(300.00));
        withdrawRequest.setDescription("Test withdrawal");

        mockMvc.perform(post("/api/accounts/me/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(2L))
                .andExpect(jsonPath("$.transactionType").value("WITHDRAW"))
                .andExpect(jsonPath("$.amount").value(300.00))
                .andExpect(jsonPath("$.message").value("Withdrawal successful"));

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Edge: Should return 401 when withdrawing without authentication")
    void testWithdraw_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/accounts/me/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isUnauthorized());

        verify(accountService, never()).withdraw(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 400 when insufficient balance")
    void testWithdraw_InsufficientBalance() throws Exception {
        when(accountService.withdraw(any(TransactionRequest.class)))
                .thenThrow(new InsufficientBalanceException());

        mockMvc.perform(post("/api/accounts/me/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 400 when withdraw amount is invalid")
    void testWithdraw_InvalidAmount() throws Exception {
        when(accountService.withdraw(any(TransactionRequest.class)))
                .thenThrow(new BusinessRuleViolationException("Amount must be greater than zero"));

        mockMvc.perform(post("/api/accounts/me/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 400 when account is inactive for withdrawal")
    void testWithdraw_InactiveAccount() throws Exception {
        when(accountService.withdraw(any(TransactionRequest.class)))
                .thenThrow(new AccountInactiveException());

        mockMvc.perform(post("/api/accounts/me/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    // ===================== GET TRANSACTIONS - HAPPY PATH =====================

    @Test
    @WithMockUser
    @DisplayName("Happy: Should get transaction history successfully")
    void testGetMyTransactions_Success() throws Exception {
        when(accountService.getTransactionHistory()).thenReturn(transactionHistoryResponse);

        mockMvc.perform(get("/api/accounts/me/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(2))
                .andExpect(jsonPath("$.transactions[0].transactionId").value(1L))
                .andExpect(jsonPath("$.transactions[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactions[1].transactionId").value(2L))
                .andExpect(jsonPath("$.transactions[1].transactionType").value("WITHDRAW"));

        verify(accountService, times(1)).getTransactionHistory();
    }

    @Test
    @DisplayName("Edge: Should return 401 when getting transactions without authentication")
    void testGetMyTransactions_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/accounts/me/transactions"))
                .andExpect(status().isUnauthorized());

        verify(accountService, never()).getTransactionHistory();
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return empty transaction history")
    void testGetMyTransactions_Empty() throws Exception {
        TransactionHistoryResponse emptyResponse = TransactionHistoryResponse.builder()
                .accountId(1L)
                .transactions(Collections.emptyList())
                .build();

        when(accountService.getTransactionHistory()).thenReturn(emptyResponse);

        mockMvc.perform(get("/api/accounts/me/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(0));

        verify(accountService, times(1)).getTransactionHistory();
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 404 when account not found for transaction history")
    void testGetMyTransactions_AccountNotFound() throws Exception {
        when(accountService.getTransactionHistory())
                .thenThrow(new ResourceNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/me/transactions"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getTransactionHistory();
    }

    // ===================== CONTENT TYPE VALIDATION =====================

    @Test
    @WithMockUser
    @DisplayName("Edge: Should return 415 when content type is not JSON for deposit")
    void testDeposit_InvalidContentType() throws Exception {
        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid content"))
                .andExpect(status().isUnsupportedMediaType());

        verify(accountService, never()).deposit(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should handle very large transaction amount")
    void testDeposit_VeryLargeAmount() throws Exception {
        TransactionRequest largeRequest = new TransactionRequest();
        largeRequest.setAmount(new BigDecimal("99999999999999999.99"));
        largeRequest.setDescription("Large deposit");

        TransactionResponse largeResponse = TransactionResponse.builder()
                .transactionId(3L)
                .accountId(1L)
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("99999999999999999.99"))
                .transactionDate(LocalDateTime.now())
                .message("Deposit successful")
                .build();

        when(accountService.deposit(any(TransactionRequest.class))).thenReturn(largeResponse);

        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(largeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(new BigDecimal("99999999999999999.99")));

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Edge: Should handle transaction with null description")
    void testDeposit_NullDescription() throws Exception {
        TransactionRequest nullDescRequest = new TransactionRequest();
        nullDescRequest.setAmount(BigDecimal.valueOf(100.00));
        nullDescRequest.setDescription(null);

        TransactionResponse response = TransactionResponse.builder()
                .transactionId(4L)
                .accountId(1L)
                .transactionType(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .description(null)
                .message("Deposit successful")
                .build();

        when(accountService.deposit(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/accounts/me/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullDescRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").doesNotExist());

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }
}