package com.company.digisavingscore.service;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Guddati Venkata Sri Sumedha
 */
class AdminServiceTest {

    private UserRepository userRepository;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        adminService = new AdminService(userRepository);
    }

    @Test
    void testPromoteUserToAdmin_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRole(Role.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        adminService.promoteUserToAdmin(userId);

        assertEquals(Role.ADMIN, user.getRole(), "User role should be ADMIN");

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testPromoteUserToAdmin_UserNotFound() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                adminService.promoteUserToAdmin(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}

