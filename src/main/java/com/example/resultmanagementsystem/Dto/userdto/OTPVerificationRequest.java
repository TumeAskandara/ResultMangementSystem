package com.example.resultmanagementsystem.Dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationRequest {
    private String email;
    private String otp;
}