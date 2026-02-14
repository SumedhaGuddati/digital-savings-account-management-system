package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.entity.enums.AccountType;
import com.company.digisavingscore.service.IAccountApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private com.company.digisavingscore.security.JwtUtils jwtUtils;

    @MockBean
    private com.company.digisavingscore.security.JwtAuthFilter jwtAuthFilter;

    private AccountApplicationRequest validRequest;
    private AccountApplicationResponse mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = new AccountApplicationRequest();
        validRequest.setAccountType(AccountType.ZETA_PRIME);
        validRequest.setAnnualIncome(Double.valueOf(800000.0));
        validRequest.setOccupation("SALARIED");
        validRequest.setDob("1995-01-01");

        validRequest.setIncomeSource("SALARY");

        validRequest.setParentName("John Doe");

        mockResponse = AccountApplicationResponse.builder()
                .id(Long.valueOf(101L))
                .status("PENDING")
                .accountType("ZETA_PRIME")
                .applicationDate(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldCreateApplicationSuccessfully() throws Exception {
        when(service.applyForAccount(any(AccountApplicationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/accounts/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturnApplicationStatus() throws Exception {
        when(service.getApplicationStatus()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/accounts/application/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}