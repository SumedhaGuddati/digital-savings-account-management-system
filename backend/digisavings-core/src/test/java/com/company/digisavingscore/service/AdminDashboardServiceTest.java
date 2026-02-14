package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.AdminDashboardStatsResponse;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServiceTest {

    @Mock
    private AccountApplicationRepository applicationRepository;
    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminDashboardService dashboardService;

    @Test
    @DisplayName("Get Stats: Success - Should calculate growth and aggregate counts")
    void getDashboardStats_Success() {
        // Arrange - Managed Assets & Growth (Previous: 1000, Current: 1100 = 10% growth)
        BigDecimal currentAssets = new BigDecimal("1100.00");
        BigDecimal previousAssets = new BigDecimal("1000.00");
        when(savingsAccountRepository.getTotalBalance()).thenReturn(currentAssets);
        when(savingsAccountRepository.getTotalBalanceForPeriod(any(), any())).thenReturn(previousAssets);

        // Arrange - Application & User counts
        when(applicationRepository.countByStatus(ApplicationStatus.APPROVED)).thenReturn(50L);
        when(applicationRepository.countByStatus(ApplicationStatus.PENDING)).thenReturn(10L);
        when(userRepository.countByRole(Role.USER)).thenReturn(100L);
        when(userRepository.countNewUsersTodayByRole(anyString())).thenReturn(5L);
        when(savingsAccountRepository.getAverageInterestRate()).thenReturn(3.5);

        // Arrange - Role Distribution (Simulating List<Object[]>)
        List<Object[]> roleCounts = new ArrayList<>();
        roleCounts.add(new Object[]{Role.ADMIN, 2L});
        roleCounts.add(new Object[]{Role.USER, 100L});
        when(userRepository.countUsersByRole()).thenReturn(roleCounts);

        // Act
        AdminDashboardStatsResponse stats = dashboardService.getDashboardStats();

        // Assert
        assertNotNull(stats);
        assertEquals(1100.0, stats.getManagedAssets().doubleValue());
        assertEquals(10.0, stats.getManagedAssetsGrowth()); // (1100-1000)/1000 * 100
        assertEquals(50L, stats.getApprovedAppsCount());
        assertEquals(2L, stats.getAdminCount());
        assertEquals(100L, stats.getCustomerCount());
        assertEquals(5, stats.getNewUsersToday());
    }

    @Test
    @DisplayName("Get Stats: Edge Case - Null Handlers")
    void getDashboardStats_HandleNulls() {
        // Arrange - Return nulls for everything
        when(savingsAccountRepository.getTotalBalance()).thenReturn(null);
        when(savingsAccountRepository.getTotalBalanceForPeriod(any(), any())).thenReturn(null);
        when(applicationRepository.countByStatus(any())).thenReturn(null);
        when(userRepository.countByRole(any())).thenReturn(null);
        when(userRepository.countNewUsersTodayByRole(anyString())).thenReturn(null);
        when(savingsAccountRepository.getAverageInterestRate()).thenReturn(null);
        when(userRepository.countUsersByRole()).thenReturn(new ArrayList<>());

        // Act
        AdminDashboardStatsResponse stats = dashboardService.getDashboardStats();

        // Assert
        assertEquals(BigDecimal.ZERO, stats.getManagedAssets());
        assertEquals(0.0, stats.getManagedAssetsGrowth());
        assertEquals(0L, stats.getApprovedAppsCount());
        assertEquals(0, stats.getNewUsersToday());
    }

    @Test
    @DisplayName("Get Stats: Growth Calculation - Zero Previous Assets")
    void getDashboardStats_ZeroPreviousGrowth() {
        // Arrange - Previous assets is zero (new system)
        when(savingsAccountRepository.getTotalBalance()).thenReturn(new BigDecimal("5000"));
        when(savingsAccountRepository.getTotalBalanceForPeriod(any(), any())).thenReturn(BigDecimal.ZERO);

        // Mock remaining to prevent NPE
        when(userRepository.countUsersByRole()).thenReturn(new ArrayList<>());

        // Act
        AdminDashboardStatsResponse stats = dashboardService.getDashboardStats();

        // Assert
        assertEquals(0.0, stats.getManagedAssetsGrowth(), "Growth should be 0 if previous period had no assets");
    }

    @Test
    @DisplayName("Get Stats: Failure - Repository Exception")
    void getDashboardStats_ThrowsException() {
        // Arrange
        when(savingsAccountRepository.getTotalBalance()).thenThrow(new RuntimeException("DB Connection Failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dashboardService.getDashboardStats());
    }
}