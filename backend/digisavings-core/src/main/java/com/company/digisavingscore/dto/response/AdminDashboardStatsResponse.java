package com.company.digisavingscore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStatsResponse {
    private BigDecimal managedAssets;
    private Double managedAssetsGrowth;
    private Long approvedAppsCount;
    private Long activeUsersCount;
    private Integer newUsersToday;
    private Double averageYield;
    private Long pendingAppsCount;
    private Long adminCount;
    private Long customerCount;
}