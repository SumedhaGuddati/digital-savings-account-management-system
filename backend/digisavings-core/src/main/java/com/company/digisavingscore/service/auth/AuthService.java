package com.company.digisavingscore.service.auth;

import com.company.digisavingscore.dto.request.LoginRequest;
import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.dto.response.auth.AuthResponse;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.exception.EmailAlreadyExistsException;
import com.company.digisavingscore.exception.InvalidCredentialsException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.UserMapper;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.security.JwtUtils;
import com.company.digisavingscore.service.EmailService;
import com.company.digisavingscore.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final OtpService otpService;
    private final EmailService emailService;

    public String sendOtp(String email) {
        log.info("Attempting to send OTP to email: {}", email);
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already registered: {}", email);
            throw new EmailAlreadyExistsException("Email already registered");
        }
        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
        log.info("OTP sent successfully to email: {}", email);
        return "OTP sent to email";
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        log.info("Signup attempt for email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup failed. Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already registered");
        }
//       if (!otpService.validateOtp(request.getEmail(), request.getOtp())) {
//           throw new InvalidCredentialsException("Invalid or expired OTP");
//       }

        User user = userMapper.toEntity(request, passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info("User saved with id: {}", user.getId());

        String token = jwtUtils.generateToken(user);
        log.debug("JWT token generated for user id {}: {}", user.getId(), token);

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed. Invalid email: {}", request.getEmail());
                    return new InvalidCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed. Incorrect password for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user);
        log.info("Login successful for user id {}: token generated", user.getId());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public String sendForgotPasswordOtp(String email) {
        log.info("Sending forgot-password OTP to email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Forgot-password failed. Email not found: {}", email);
                    return new ResourceNotFoundException("Email not found");
                });

        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
        log.info("Forgot-password OTP sent to email: {}", email);

        return "OTP sent for password reset";
    }

    public String resetPassword(String email, String otp, String newPassword) {
        log.info("Reset password attempt for email: {}", email);
        if (!otpService.validateOtp(email, otp)) {
            log.warn("Invalid OTP for password reset for email: {}", email);
            throw new InvalidCredentialsException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Password reset failed. Email not found: {}", email);
                    return new ResourceNotFoundException("Email not found");
                });

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successfully for user id: {}", user.getId());

        return "Password reset successfully";
    }
}
