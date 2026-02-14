package com.company.digisavingscore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@Service
@Slf4j
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> expiryStorage = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        expiryStorage.put(email, LocalDateTime.now().plusMinutes(5));
        log.info("Generated OTP for email {}: {}", email, otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        log.info("Validating OTP for email {}: {}", email, otp);

        if (!otpStorage.containsKey(email)) {
            log.warn("No OTP found for email {}", email);
            return false;
        }

        if (expiryStorage.get(email).isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);
            expiryStorage.remove(email);
            log.warn("OTP for email {} has expired", email);
            return false;
        }

        boolean isValid = otpStorage.get(email).equals(otp);

        if (isValid) {
            otpStorage.remove(email);
            expiryStorage.remove(email);
            log.info("OTP for email {} is valid and has been removed", email);
        } else {
            log.warn("Invalid OTP entered for email {}", email);
        }

        return isValid;
    }
}
