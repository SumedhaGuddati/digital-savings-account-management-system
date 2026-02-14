package com.company.digisavingscore.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> expiryStorage = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        expiryStorage.put(email, LocalDateTime.now().plusMinutes(5));
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;

        if (expiryStorage.get(email).isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);
            expiryStorage.remove(email);
            return false;
        }

        boolean isValid = otpStorage.get(email).equals(otp);

        if (isValid) {
            otpStorage.remove(email);
            expiryStorage.remove(email);
        }

        return isValid;
    }
}
