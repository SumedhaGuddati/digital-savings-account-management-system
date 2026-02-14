package com.company.digisavingscore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        log.info("Sending OTP to email: {}", toEmail);

        String subject = "OTP for Registration";
        String message = "Your OTP is: " + otp +
                "\nThis OTP will expire in 5 minutes.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            log.info("OTP sent successfully to email: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP to email: {}", toEmail, e);
            throw e;
        }
    }
}
