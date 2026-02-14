package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.response.AdminDashboardStatsResponse;
import com.company.digisavingscore.service.AdminDashboardService;
import com.company.digisavingscore.security.JwtUtils;
import com.company.digisavingscore.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AdminDashboardController.class, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminDashboardService dashboardService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getDashboardStats_Success() throws Exception {
        // Arrange
        AdminDashboardStatsResponse mockStats = AdminDashboardStatsResponse.builder()
                .managedAssets(new BigDecimal("1500000.00"))
                .managedAssetsGrowth(12.5)
                .approvedAppsCount(150L)
                .activeUsersCount(1200L)
                .newUsersToday(10)
                .averageYield(4.5)
                .pendingAppsCount(25L)
                .adminCount(5L)
                .customerCount(2000L)
                .build();

        when(dashboardService.getDashboardStats()).thenReturn(mockStats);

        // Act & Assert
        mockMvc.perform(get("/api/admin/dashboard/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managedAssets").value(1500000.00))
                .andExpect(jsonPath("$.activeUsersCount").value(1200))
                .andExpect(jsonPath("$.pendingAppsCount").value(25))
                .andExpect(jsonPath("$.newUsersToday").value(10));
    }

    @Test
    void getDashboardStats_EmptyData() throws Exception {
        // Arrange
        AdminDashboardStatsResponse emptyStats = AdminDashboardStatsResponse.builder()
                .managedAssets(BigDecimal.ZERO)
                .activeUsersCount(0L)
                .pendingAppsCount(0L)
                .build();

        when(dashboardService.getDashboardStats()).thenReturn(emptyStats);

        // Act & Assert
        mockMvc.perform(get("/api/admin/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managedAssets").value(0))
                .andExpect(jsonPath("$.activeUsersCount").value(0));
    }

    @Test
    void getDashboardStats_ServiceError() throws Exception {
        // Arrange
        when(dashboardService.getDashboardStats()).thenThrow(new RuntimeException("Service Unavailable"));

        // Act & Assert
        mockMvc.perform(get("/api/admin/dashboard/stats"))
                .andExpect(status().isInternalServerError());
    }
}