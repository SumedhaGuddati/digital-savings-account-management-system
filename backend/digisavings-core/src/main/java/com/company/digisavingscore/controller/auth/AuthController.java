package com.company.digisavingscore.controller.auth;

import com.company.digisavingscore.dto.request.LoginRequest;
import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.dto.response.auth.AuthResponse;
import com.company.digisavingscore.service.auth.AuthService;
import com.company.digisavingscore.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendOtp(email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Boolean>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean valid = otpService.validateOtp(email, otp);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<String> sendForgotPasswordOtp(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendForgotPasswordOtp(email));
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<Map<String, Boolean>> verifyForgotPasswordOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean valid = otpService.validateOtp(email, otp);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");
        return ResponseEntity.ok(authService.resetPassword(email, otp, newPassword));
    }
}
