package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.UserResponse;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testGetAllUsers_ReturnsUserResponses() {
        User user1 = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .role(Role.USER)
                .createdAt(LocalDateTime.of(2026, 2, 13, 10, 0))
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("Bob")
                .email("bob@example.com")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.of(2026, 2, 12, 12, 30))
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserResponse> responses = userService.getAllUsers();

        assertEquals(2, responses.size(), "Should return two users");

        UserResponse response1 = responses.get(0);
        assertEquals(user1.getId(), response1.getId());
        assertEquals(user1.getName(), response1.getName());
        assertEquals(user1.getEmail(), response1.getEmail());
        assertEquals(user1.getRole().name(), response1.getRole());
        assertEquals(user1.getCreatedAt(), response1.getCreatedAt());

        UserResponse response2 = responses.get(1);
        assertEquals(user2.getId(), response2.getId());
        assertEquals(user2.getName(), response2.getName());
        assertEquals(user2.getEmail(), response2.getEmail());
        assertEquals(user2.getRole().name(), response2.getRole());
        assertEquals(user2.getCreatedAt(), response2.getCreatedAt());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());
        List<UserResponse> responses = userService.getAllUsers();
        assertNotNull(responses, "Response list should not be null");
        assertTrue(responses.isEmpty(), "Response list should be empty");
        verify(userRepository, times(1)).findAll();
    }
}

