package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.service.IAdminAccountService;
import com.company.digisavingscore.security.JwtUtils;
import com.company.digisavingscore.security.CustomUserDetailsService;
import com.company.digisavingscore.exception.ResourceNotFoundException; // Assume custom exception exists
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Bharadi Harshitha
 */
@WebMvcTest(value = AdminAccountController.class, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
class AdminAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAdminAccountService adminAccountService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    // --- Success Edge Cases ---

    @Test
    @DisplayName("Edge Case: Get Pending Applications - Return Empty List")
    void getPendingApplications_ShouldReturnEmptyList_WhenNoPendingApps() throws Exception {
        when(adminAccountService.getPendingApplications()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/applications/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- Error Edge Cases ---

    @Test
    @DisplayName("Edge Case: Approve Application - Resource Not Found")
    void approveApplication_ShouldReturn404_WhenApplicationIdDoesNotExist() throws Exception {
        AccountApprovalRequest request = new AccountApprovalRequest();
        request.setDecision("APPROVE");

        // Mocking a service exception for a non-existent ID
        when(adminAccountService.approveApplication(eq(999L), any(), any()))
                .thenThrow(new ResourceNotFoundException("Application not found with id: 999"));

        mockMvc.perform(post("/api/admin/applications/999/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Edge Case: Approve Application - Bad Request (Null Body)")
    void approveApplication_ShouldReturn400_WhenRequestBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/admin/applications/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")) // Empty body
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Edge Case: Reject Application - Internal Server Error")
    void rejectApplication_ShouldReturn500_WhenServiceThrowsUnexpectedException() throws Exception {
        AccountApprovalRequest request = new AccountApprovalRequest();
        request.setDecision("REJECT");

        when(adminAccountService.rejectApplication(anyLong(), any(), any()))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/api/admin/applications/1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Edge Case: Wrong HTTP Method")
    void approveApplication_WithGetMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // Trying to use GET on an endpoint that only supports POST
        mockMvc.perform(get("/api/admin/applications/1/approve"))
                .andExpect(status().isMethodNotAllowed());
    }
}