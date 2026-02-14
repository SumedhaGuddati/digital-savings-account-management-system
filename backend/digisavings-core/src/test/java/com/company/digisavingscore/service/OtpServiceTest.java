package com.company.digisavingscore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Guddati Venkata Sri Sumedha
 */
class OtpServiceTest {

    private OtpService otpService;

    @BeforeEach
    void setUp() {
        otpService = new OtpService();
    }

    @Test
    void generateOtp_ShouldReturnSixDigitNumber() {
        String email = "user@example.com";
        String otp = otpService.generateOtp(email);
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    void validateOtp_ShouldReturnTrue_ForValidOtp() {
        String email = "user@example.com";
        String otp = otpService.generateOtp(email);
        boolean result = otpService.validateOtp(email, otp);
        assertTrue(result);
    }

    @Test
    void validateOtp_ShouldReturnFalse_WhenOtpUsedTwice() {
        String email = "user@example.com";
        String otp = otpService.generateOtp(email);
        assertTrue(otpService.validateOtp(email, otp));
        assertFalse(otpService.validateOtp(email, otp));
    }

    @Test
    void validateOtp_ShouldReturnFalse_ForWrongOtp() {
        String email = "user@example.com";
        otpService.generateOtp(email);
        boolean result = otpService.validateOtp(email, "123456");
        assertFalse(result);
    }

    @Test
    void validateOtp_ShouldReturnFalse_WhenNoOtpGenerated() {
        boolean result = otpService.validateOtp("unknown@example.com", "123456");
        assertFalse(result);
    }

    @Test
    void validateOtp_ShouldReturnFalse_WhenOtpExpired() throws Exception {
        String email = "user@example.com";
        String otp = otpService.generateOtp(email);
        Field expiryField = OtpService.class.getDeclaredField("expiryStorage");
        expiryField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, LocalDateTime> expiryStorage =
                (Map<String, LocalDateTime>) expiryField.get(otpService);

        expiryStorage.put(email, LocalDateTime.now().minusMinutes(1));
        boolean result = otpService.validateOtp(email, otp);
        assertFalse(result);
    }
}
