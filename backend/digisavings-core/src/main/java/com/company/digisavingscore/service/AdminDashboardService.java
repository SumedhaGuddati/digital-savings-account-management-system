package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.AdminDashboardStatsResponse;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private final AccountApplicationRepository applicationRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final UserRepository userRepository;

    public AdminDashboardStatsResponse getDashboardStats() {
        log.info("Fetching admin dashboard statistics");

        try {
            // Get current month and previous month dates
            LocalDate now = LocalDate.now();
            LocalDate firstDayOfCurrentMonth = now.withDayOfMonth(1);
            LocalDate firstDayOfPreviousMonth = firstDayOfCurrentMonth.minusMonths(1);
            LocalDate lastDayOfPreviousMonth = firstDayOfCurrentMonth.minusDays(1);

            // Calculate managed assets
            BigDecimal currentManagedAssets = savingsAccountRepository.getTotalBalance();
            BigDecimal previousManagedAssets = savingsAccountRepository.getTotalBalanceForPeriod(
                    firstDayOfPreviousMonth, lastDayOfPreviousMonth);

            Double managedAssetsGrowth = calculateGrowthPercentage(previousManagedAssets, currentManagedAssets);

            // Get counts - FIXED: Use Role enum directly, not String
            Long approvedAppsCount = applicationRepository.countByStatus(ApplicationStatus.APPROVED);
            Long activeUsersCount = userRepository.countByRole(Role.USER); // This expects Role enum
            Long newUsersToday = userRepository.countNewUsersTodayByRole(Role.USER.name()); // This expects String
            Double averageYield = savingsAccountRepository.getAverageInterestRate();
            Long pendingAppsCount = applicationRepository.countByStatus(ApplicationStatus.PENDING);

            // NEW: Get role distribution
            Long adminCount = 0L;
            Long customerCount = 0L;

            List<Object[]> roleCounts = userRepository.countUsersByRole();
            for (Object[] roleCount : roleCounts) {
                Role role = (Role) roleCount[0];
                Long count = (Long) roleCount[1];

                if (role == Role.ADMIN) {
                    adminCount = count;
                } else if (role == Role.USER) {
                    customerCount = count;
                }
            }

            // Handle null values
            if (averageYield == null) averageYield = 0.0;
            if (newUsersToday == null) newUsersToday = 0L;
            if (currentManagedAssets == null) currentManagedAssets = BigDecimal.ZERO;
            if (previousManagedAssets == null) previousManagedAssets = BigDecimal.ZERO;
            if (approvedAppsCount == null) approvedAppsCount = 0L;
            if (activeUsersCount == null) activeUsersCount = 0L;
            if (pendingAppsCount == null) pendingAppsCount = 0L;
            if (adminCount == null) adminCount = 0L;
            if (customerCount == null) customerCount = 0L;

            return AdminDashboardStatsResponse.builder()
                    .managedAssets(currentManagedAssets)
                    .managedAssetsGrowth(managedAssetsGrowth)
                    .approvedAppsCount(approvedAppsCount)
                    .activeUsersCount(activeUsersCount)
                    .newUsersToday(newUsersToday.intValue())
                    .averageYield(averageYield)
                    .pendingAppsCount(pendingAppsCount)
                    .adminCount(adminCount)
                    .customerCount(customerCount)
                    .build();

        } catch (Exception e) {
            log.error("Error fetching dashboard stats: ", e);
            throw e;
        }
    }

    private Double calculateGrowthPercentage(BigDecimal previous, BigDecimal current) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}