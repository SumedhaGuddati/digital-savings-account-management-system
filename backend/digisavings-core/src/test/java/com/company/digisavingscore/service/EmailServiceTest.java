package com.company.digisavingscore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);
    }

    @Test
    void testSendOtp() {
        String email = "test@example.com";
        String otp = "123456";

        emailService.sendOtp(email, otp);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals(email, sentMessage.getTo()[0], "Recipient email should match");
        assertEquals("OTP for Registration", sentMessage.getSubject(), "Subject should match");
        assertTrue(sentMessage.getText().contains(otp), "OTP should be in the message body");
        assertTrue(sentMessage.getText().contains("expire in 5 minutes"), "Message should mention expiration");
    }
}

