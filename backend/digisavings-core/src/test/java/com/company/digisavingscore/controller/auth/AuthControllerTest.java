package com.company.digisavingscore.controller.auth;

import com.company.digisavingscore.dto.request.LoginRequest;
import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.dto.response.auth.AuthResponse;
import com.company.digisavingscore.service.OtpService;
import com.company.digisavingscore.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Guddati Venkata Sri Sumedha
 */
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOtp() {
        String email = "test@example.com";
        when(authService.sendOtp(email)).thenReturn("OTP sent");

        ResponseEntity<String> response = authController.sendOtp(email);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("OTP sent", response.getBody());
        verify(authService, times(1)).sendOtp(email);
    }

    @Test
    void testVerifyOtp() {
        String email = "test@example.com";
        String otp = "123456";
        when(otpService.validateOtp(email, otp)).thenReturn(true);

        ResponseEntity<Map<String, Boolean>> response = authController.verifyOtp(Map.of(
                "email", email,
                "otp", otp
        ));

        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody().get("valid"));
        verify(otpService, times(1)).validateOtp(email, otp);
    }

    @Test
    void testSignup() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setPassword("password");

        AuthResponse authResponse = AuthResponse.builder()
                .token("token123")
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role("USER")
                .build();

        when(authService.signup(request)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.signup(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(authResponse, response.getBody());
        verify(authService, times(1)).signup(request);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        AuthResponse authResponse = AuthResponse.builder()
                .token("token123")
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role("USER")
                .build();

        when(authService.login(request)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(authResponse, response.getBody());
        verify(authService, times(1)).login(request);
    }

    @Test
    void testSendForgotPasswordOtp() {
        String email = "test@example.com";
        when(authService.sendForgotPasswordOtp(email)).thenReturn("OTP sent for password reset");

        ResponseEntity<String> response = authController.sendForgotPasswordOtp(email);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("OTP sent for password reset", response.getBody());
        verify(authService, times(1)).sendForgotPasswordOtp(email);
    }

    @Test
    void testVerifyForgotPasswordOtp() {
        String email = "test@example.com";
        String otp = "123456";
        when(otpService.validateOtp(email, otp)).thenReturn(true);

        ResponseEntity<Map<String, Boolean>> response = authController.verifyForgotPasswordOtp(Map.of(
                "email", email,
                "otp", otp
        ));

        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody().get("valid"));
        verify(otpService, times(1)).validateOtp(email, otp);
    }

    @Test
    void testResetPassword() {
        String email = "test@example.com";
        String otp = "123456";
        String newPassword = "newPassword";

        when(authService.resetPassword(email, otp, newPassword)).thenReturn("Password reset successfully");

        ResponseEntity<String> response = authController.resetPassword(Map.of(
                "email", email,
                "otp", otp,
                "newPassword", newPassword
        ));

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Password reset successfully", response.getBody());
        verify(authService, times(1)).resetPassword(email, otp, newPassword);
    }
}
