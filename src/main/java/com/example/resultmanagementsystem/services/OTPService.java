package com.example.resultmanagementsystem.services;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OTPService {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 10;
    private final SecureRandom random = new SecureRandom();

    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public LocalDateTime getOTPExpiry() {
        return LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES);
    }

    public boolean isOTPExpired(LocalDateTime expiry) {
        return expiry == null || LocalDateTime.now().isAfter(expiry);
    }

    public boolean isOTPValid(String inputOtp, String storedOtp, LocalDateTime expiry) {
        if (inputOtp == null || storedOtp == null || isOTPExpired(expiry)) {
            return false;
        }
        return inputOtp.equals(storedOtp);
    }
}
