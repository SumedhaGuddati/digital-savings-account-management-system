package com.company.digisavingscore.service.auth;

import com.company.digisavingscore.dto.request.LoginRequest;
import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.dto.response.auth.AuthResponse;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.exception.EmailAlreadyExistsException;
import com.company.digisavingscore.exception.InvalidCredentialsException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.UserMapper;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.security.JwtUtils;
import com.company.digisavingscore.service.EmailService;
import com.company.digisavingscore.service.OtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserMapper userMapper;
    @Mock
    private OtpService otpService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@test.com");
        user.setPassword("encodedPass");
        user.setRole(Role.USER);
    }

    @Test
    void sendOtp_success() {
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(otpService.generateOtp("john@test.com")).thenReturn("123456");

        String result = authService.sendOtp("john@test.com");

        assertEquals("OTP sent to email", result);
        verify(emailService).sendOtp("john@test.com", "123456");
    }

    @Test
    void sendOtp_emailExists() {
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.sendOtp("john@test.com"));
    }

    @Test
    void signup_success() {
        SignupRequest request = new SignupRequest();
        request.setName("John");
        request.setEmail("john@test.com");
        request.setPassword("password");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userMapper.toEntity(eq(request), anyString())).thenReturn(user);
        when(jwtUtils.generateToken(user)).thenReturn("jwtToken");

        AuthResponse response = authService.signup(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("USER", response.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void signup_emailExists() {
        SignupRequest request = new SignupRequest();
        request.setEmail("john@test.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.signup(request));
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPass"))
                .thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("jwtToken");

        AuthResponse response = authService.login(request);

        assertEquals("jwtToken", response.getToken());
        assertEquals("USER", response.getRole());
    }

    @Test
    void login_invalidEmail() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@test.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void login_invalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("wrong");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPass"))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void forgotPassword_success() {
        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));
        when(otpService.generateOtp("john@test.com"))
                .thenReturn("123456");

        String result = authService.sendForgotPasswordOtp("john@test.com");

        assertEquals("OTP sent for password reset", result);
        verify(emailService).sendOtp("john@test.com", "123456");
    }

    @Test
    void forgotPassword_emailNotFound() {
        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.sendForgotPasswordOtp("john@test.com"));
    }

    @Test
    void resetPassword_success() {
        when(otpService.validateOtp("john@test.com", "123456"))
                .thenReturn(true);
        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass"))
                .thenReturn("encodedNewPass");

        String result = authService.resetPassword("john@test.com", "123456", "newPass");

        assertEquals("Password reset successfully", result);
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_invalidOtp() {
        when(otpService.validateOtp("john@test.com", "123456"))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.resetPassword("john@test.com", "123456", "newPass"));
    }

    @Test
    void resetPassword_emailNotFound() {
        when(otpService.validateOtp("john@test.com", "123456"))
                .thenReturn(true);
        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.resetPassword("john@test.com", "123456", "newPass"));
    }
}
