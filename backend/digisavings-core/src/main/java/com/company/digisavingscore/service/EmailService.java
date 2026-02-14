package com.company.digisavingscore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        log.info("Sending OTP to email: {}", toEmail);

        String subject = "Your ZetaRise Verification Code";
        String message = "Hello,\n\n" +
                "Your verification code is: " + otp + "\n\n" +
                "This code will expire in 5 minutes.\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Team ZetaRise";

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

    public void sendApplicationApprovalEmail(String toEmail, String userName, String accountNumber) {
        log.info("Sending approval email to: {}", toEmail);

        String subject = "🎉 Your ZetaRise Account is Approved!";
        String message = "Dear " + userName + ",\n\n" +
                "Congratulations! Your savings account application has been approved.\n\n" +
                "Account Number: " + accountNumber + "\n" +
                "Status: Active\n\n" +
                "You can now log in to your account and start managing your finances.\n\n" +
                "Thank you for choosing ZetaRise!\n\n" +
                "Best regards,\n" +
                "Team ZetaRise";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            log.info("Approval email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send approval email to: {}", toEmail, e);
        }
    }

    public void sendApplicationRejectionEmail(String toEmail, String userName) {
        log.info("Sending rejection email to: {}", toEmail);

        String subject = "ZetaRise Account Application Update";
        String message = "Dear " + userName + ",\n\n" +
                "Thank you for your interest in ZetaRise.\n\n" +
                "Unfortunately, we are unable to approve your savings account application at this time.\n\n" +
                "If you have any questions or would like to reapply, please contact our support team.\n\n" +
                "We appreciate your understanding.\n\n" +
                "Best regards,\n" +
                "Team ZetaRise";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            log.info("Rejection email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send rejection email to: {}", toEmail, e);
        }
    }
}
