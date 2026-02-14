package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.response.AuditEventResponse;
import com.company.digisavingscore.service.AuditService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuditController.class, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAuditLogs_Success() throws Exception {
        // Arrange
        AuditEventResponse response = AuditEventResponse.builder()
                .id(1L)
                .timestamp(LocalDateTime.now())
                .username("admin_user")
                .userEmail("admin@company.com")
                .action("APPROVE_ACCOUNT")
                .severity("INFO")
                .ipAddress("192.168.1.1")
                .build();

        when(auditService.getAuditLogs(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/admin/audit/logs")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin_user"))
                .andExpect(jsonPath("$[0].action").value("APPROVE_ACCOUNT"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAuditLogsCount_Success() throws Exception {
        // Arrange
        when(auditService.getAuditLogsCount(any(), any(), any(), any()))
                .thenReturn(100L);

        // Act & Assert
        mockMvc.perform(get("/api/admin/audit/logs/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(100));
    }

    @Test
    void getEventTypes_Success() throws Exception {
        // Arrange
        List<String> eventTypes = List.of("LOGIN", "ACCOUNT_CREATION", "DEPOSIT");
        when(auditService.getEventTypes()).thenReturn(eventTypes);

        // Act & Assert
        mockMvc.perform(get("/api/admin/audit/event-types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("LOGIN"));
    }

    // --- Edge Cases ---

    @Test
    void getAuditLogs_EmptyResults() throws Exception {
        when(auditService.getAuditLogs(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/audit/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAuditLogs_InvalidDateParam_BadRequest() throws Exception {
        // Passing an invalid date string should trigger a 400 Bad Request
        mockMvc.perform(get("/api/admin/audit/logs")
                        .param("startDate", "invalid-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAuditLogs_InternalServerError() throws Exception {
        when(auditService.getAuditLogs(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database timeout"));

        mockMvc.perform(get("/api/admin/audit/logs"))
                .andExpect(status().isInternalServerError());
    }
}