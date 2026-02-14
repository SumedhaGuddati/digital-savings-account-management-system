package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountType;
import com.company.digisavingscore.exception.AccountAlreadyExistsException;
import com.company.digisavingscore.mapper.AccountApplicationMapper;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.util.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountApplicationServiceTest {

    @Mock
    private AccountApplicationRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountApplicationMapper mapper;

    @InjectMocks
    private AccountApplicationService service;

    private MockedStatic<SecurityUtils> mockedSecurityUtils;
    private User mockUser;
    private AccountApplicationRequest validRequest;

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn("test@example.com");

        mockUser = new User();
        mockUser.setId(Long.valueOf(1L));
        mockUser.setEmail("test@example.com");

        validRequest = new AccountApplicationRequest();
        validRequest.setAccountType(AccountType.ZETA_PRIME);
        validRequest.setDob("1995-01-01");
        validRequest.setAnnualIncome(Double.valueOf(500000.0));
        validRequest.setOccupation("ENGINEER");
        validRequest.setIncomeSource("SALARY");
    }

    @AfterEach
    void tearDown() {
        if (mockedSecurityUtils != null) {
            mockedSecurityUtils.close();
        }
    }

    @Test
    void applyForAccount_Success() {
        Long uId = Long.valueOf(1L);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(repository.findByUserId(uId)).thenReturn(Optional.empty());
        when(userRepository.findById(uId)).thenReturn(Optional.of(mockUser));
        when(repository.save(any(AccountApplication.class))).thenReturn(new AccountApplication());
        when(mapper.toResponse(any())).thenReturn(AccountApplicationResponse.builder().status("PENDING").build());

        AccountApplicationResponse response = service.applyForAccount(validRequest);

        assertNotNull(response);
        assertEquals("PENDING", response.getStatus());
        verify(repository, times(1)).save(any());
    }

    @Test
    void applyForAccount_ThrowsException_WhenDuplicate() {
        Long uId = Long.valueOf(1L);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(repository.findByUserId(uId)).thenReturn(Optional.of(new AccountApplication()));

        assertThrows(AccountAlreadyExistsException.class, () -> service.applyForAccount(validRequest));
        verify(repository, never()).save(any());
    }

    @Test
    void getApplicationStatus_Success() {
        Long uId = Long.valueOf(1L);
        AccountApplication application = new AccountApplication();
        application.setUser(mockUser);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(repository.findByUserId(uId)).thenReturn(Optional.of(application));
        when(mapper.toResponse(any())).thenReturn(AccountApplicationResponse.builder().id(Long.valueOf(100L)).build());

        AccountApplicationResponse response = service.getApplicationStatus();

        assertNotNull(response);
        verify(repository, times(1)).findByUserId(uId);
    }
}