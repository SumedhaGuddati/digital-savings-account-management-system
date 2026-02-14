package com.company.digisavingscore.controller;

import com.company.digisavingscore.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Guddati Venkata Sri Sumedha
 */
class AdminControllerTest {

    private AdminService adminService;
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        adminService = mock(AdminService.class);
        adminController = new AdminController(adminService);
    }

    @Test
    void testPromoteUserToAdmin_Success() {
        Long userId = 42L;
        doNothing().when(adminService).promoteUserToAdmin(userId);
        ResponseEntity<String> response = adminController.promoteUserToAdmin(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User promoted to ADMIN successfully.", response.getBody());
        verify(adminService, times(1)).promoteUserToAdmin(userId);
    }

    @Test
    void testPromoteUserToAdmin_ServiceThrowsException() {
        Long userId = 99L;
        doThrow(new RuntimeException("User not found")).when(adminService).promoteUserToAdmin(userId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminController.promoteUserToAdmin(userId);
        });
        assertEquals("User not found", exception.getMessage());
        verify(adminService, times(1)).promoteUserToAdmin(userId);
    }
}

