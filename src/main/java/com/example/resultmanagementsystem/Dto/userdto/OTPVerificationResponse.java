package com.example.resultmanagementsystem.Dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationResponse {
    private String token;
    private String message;
    private boolean success;
    private Object userDetails; // This can be a User object or any other relevant data type
}