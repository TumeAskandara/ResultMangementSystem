package com.example.resultmanagementsystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {
        String verificationLink = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Email");
        message.setText("Click the link to verify your email: " + verificationLink);

        mailSender.send(message);
    }
}
