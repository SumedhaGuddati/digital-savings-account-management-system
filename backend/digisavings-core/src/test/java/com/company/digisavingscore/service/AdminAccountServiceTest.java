package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.exception.BusinessRuleViolationException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.AdminAccountMapper;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {

    @Mock
    private AccountApplicationRepository applicationRepository;
    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @Mock
    private AdminAccountMapper adminAccountMapper;

    @InjectMocks
    private AdminAccountService adminAccountService;

    private User testUser;
    private AccountApplication pendingApplication;
    private AccountApprovalRequest approvalRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("John Doe");

        pendingApplication = AccountApplication.builder()
                .id(10L)
                .status(ApplicationStatus.PENDING)
                .user(testUser)
                .build();

        approvalRequest = new AccountApprovalRequest();
        approvalRequest.setDecision("APPROVE");
    }

    // --- Approve Application Tests ---

    @Test
    @DisplayName("Approve: Success - Should create new savings account")
    void approveApplication_Success() {
        // Arrange
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(pendingApplication));
        when(savingsAccountRepository.existsByUser(testUser)).thenReturn(false);
        when(savingsAccountRepository.save(any(SavingsAccount.class))).thenAnswer(i -> {
            SavingsAccount sa = i.getArgument(0);
            sa.setId(100L); // Simulate DB ID generation
            return sa;
        });

        // Act
        SavingsAccountResponse response = adminAccountService.approveApplication(10L, approvalRequest, "admin_user");

        // Assert
        assertNotNull(response);
        assertEquals(ApplicationStatus.APPROVED, pendingApplication.getStatus());
        assertEquals("admin_user", pendingApplication.getReviewedBy());
        assertEquals(100L, response.getId());
        assertTrue(response.getAccountNumber().startsWith("SAV-"));
        verify(applicationRepository).save(pendingApplication);
        verify(savingsAccountRepository).save(any(SavingsAccount.class));
    }

    @Test
    @DisplayName("Approve: Fail - Application Not Found")
    void approveApplication_NotFound() {
        when(applicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                adminAccountService.approveApplication(99L, approvalRequest, "admin"));
    }

    @Test
    @DisplayName("Approve: Fail - Application not in PENDING status")
    void approveApplication_NotPending() {
        pendingApplication.setStatus(ApplicationStatus.REJECTED);
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(pendingApplication));

        assertThrows(BusinessRuleViolationException.class, () ->
                adminAccountService.approveApplication(10L, approvalRequest, "admin"));
    }

    @Test
    @DisplayName("Approve: Fail - User already has an account")
    void approveApplication_AlreadyHasAccount() {
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(pendingApplication));
        when(savingsAccountRepository.existsByUser(testUser)).thenReturn(true);

        assertThrows(BusinessRuleViolationException.class, () ->
                adminAccountService.approveApplication(10L, approvalRequest, "admin"));
    }

    // --- Reject Application Tests ---

    @Test
    @DisplayName("Reject: Success - Should mark status as REJECTED")
    void rejectApplication_Success() {
        // Arrange
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(pendingApplication));

        // Act
        SavingsAccountResponse response = adminAccountService.rejectApplication(10L, approvalRequest, "admin_user");

        // Assert
        assertEquals(ApplicationStatus.REJECTED, pendingApplication.getStatus());
        assertEquals(AccountStatus.CLOSED, response.getStatus());
        assertNull(response.getAccountNumber());
        verify(applicationRepository).save(pendingApplication);
    }

    @Test
    @DisplayName("Reject: Fail - Application has no associated User")
    void rejectApplication_NoUser() {
        pendingApplication.setUser(null);
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(pendingApplication));

        assertThrows(BusinessRuleViolationException.class, () ->
                adminAccountService.rejectApplication(10L, approvalRequest, "admin"));
    }
}