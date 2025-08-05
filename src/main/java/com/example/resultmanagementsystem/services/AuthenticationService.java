//
//package com.example.resultmanagementsystem.services;
//
//import com.example.resultmanagementsystem.Dto.Repository.UserRepository;
//import com.example.resultmanagementsystem.Dto.userdto.*;
//import com.example.resultmanagementsystem.config.JWTService;
//import com.example.resultmanagementsystem.model.Role.Role;
//import com.example.resultmanagementsystem.model.User;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JWTService jwtService;
//    private final AuthenticationManager authenticationManager;
//    private final EmailService emailService;
//    private final OTPService otpService;
//    private final StudentService studentService; // ✅ Add this
//    private final TeacherService teacherService;
//
//    @Transactional
//    public AuthenticationResponse register(RegisterRequest request) {
//        // Check if user already exists
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new IllegalArgumentException("User with this email already exists");
//        }
//
//        // Generate registration OTP
//        String otp = otpService.generateOTP();
//
//        // Create user with OTP
//        var user = User.builder()
//                .firstname(request.getFirstName())
//                .lastname(request.getLastName())
//                .name(request.getFirstName() + " " + request.getLastName()) // Combined name field
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.valueOf(request.getRole().toUpperCase()))
//                .emailVerified(false)
//                .isVerified(false) // Keep for compatibility
//                .departmentId(new HashSet<>()) // Initialize empty set
//                .registrationOtp(otp)
//                .registrationOtpExpiry(otpService.getOTPExpiry())
//                .build();
//
//        userRepository.save(user);
//
//        // Send registration OTP email
//        try {
//            emailService.sendRegistrationOTP(user.getEmail(), otp, user.getFirstname());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send registration OTP email: " + e.getMessage());
//        }
//
//        return AuthenticationResponse.builder()
//                .message("Registration successful! Please check your email for OTP verification.")
//                .email(user.getEmail())
//                .requiresOtp(true)
//                .build();
//    }
//
//    @Transactional
//    public OTPVerificationResponse verifyRegistrationOTP(OTPVerificationRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        if (user.isEmailVerified()) {
//            throw new IllegalArgumentException("Email already verified");
//        }
//
//        if (!otpService.isOTPValid(request.getOtp(), user.getRegistrationOtp(), user.getRegistrationOtpExpiry())) {
//            throw new IllegalArgumentException("Invalid or expired OTP");
//        }
//
//        // Mark email as verified and clear OTP
//        user.setEmailVerified(true);
//        user.setVerified(true); // Keep for compatibility
//        user.setRegistrationOtp(null);
//        user.setRegistrationOtpExpiry(null);
//        userRepository.save(user);
//
//        // Send welcome email
//        try {
//            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstname());
//        } catch (Exception e) {
//            // Log error but don't fail the verification
//            System.err.println("Failed to send welcome email: " + e.getMessage());
//        }
//
//        // Generate JWT token
//        String jwtToken = jwtService.generateToken(user);
//
//        return OTPVerificationResponse.builder()
//                .success(true)
//                .message("Email verified successfully! Welcome to Result Management System.")
//                .token(jwtToken)
//                .build();
//    }
//
//    @Transactional
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        // Authenticate user credentials
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        if (!user.isEmailVerified() && !user.isVerified()) {
//            throw new IllegalArgumentException("Please verify your email before logging in");
//        }
//
//        // Generate and send login OTP
//        String otp = otpService.generateOTP();
//        user.setLoginOtp(otp);
//        user.setLoginOtpExpiry(otpService.getOTPExpiry());
//        user.setLoginOtpVerified(false);
//        userRepository.save(user);
//
//        try {
//            emailService.sendLoginOTP(user.getEmail(), otp, user.getFirstname());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send login OTP email: " + e.getMessage());
//        }
//
//        return AuthenticationResponse.builder()
//                .message("Login credentials verified! Please check your email for OTP.")
//                .email(user.getEmail())
//                .requiresOtp(true)
//                .build();
//    }
//
//    @Transactional
//    public OTPVerificationResponse verifyLoginOTP(OTPVerificationRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        if (!otpService.isOTPValid(request.getOtp(), user.getLoginOtp(), user.getLoginOtpExpiry())) {
//            throw new IllegalArgumentException("Invalid or expired OTP");
//        }
//
//        // Mark login OTP as verified
//        user.setLoginOtpVerified(true);
//        user.setLoginOtp(null);
//        user.setLoginOtpExpiry(null);
//        userRepository.save(user);
//
//        // Generate JWT token
//        String jwtToken = jwtService.generateToken(user);
//
//        // ✅ Get additional user details
//        Object userDetails = null;
//        switch (user.getRole()) {
//            case STUDENT -> userDetails = studentService.getStudentIdByEmail(user.getEmail());
//            case TEACHER -> userDetails = teacherService.getTeacherByEmail(user.getEmail());
//            // You can add other roles here if needed
//        }
//
//        return OTPVerificationResponse.builder()
//                .success(true)
//                .message("Login successful!")
//                .token(jwtToken)
//                .userDetails(userDetails) // ✅ Include full student/teacher info
//                .build();
//    }
//
//
//    public AuthenticationResponse resendOTP(String email, String type) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        String otp = otpService.generateOTP();
//        LocalDateTime expiry = otpService.getOTPExpiry();
//
//        if ("registration".equalsIgnoreCase(type)) {
//            if (user.isEmailVerified() || user.isVerified()) {
//                throw new IllegalArgumentException("Email already verified");
//            }
//            user.setRegistrationOtp(otp);
//            user.setRegistrationOtpExpiry(expiry);
//            emailService.sendRegistrationOTP(email, otp, user.getFirstname());
//        } else if ("login".equalsIgnoreCase(type)) {
//            user.setLoginOtp(otp);
//            user.setLoginOtpExpiry(expiry);
//            user.setLoginOtpVerified(false);
//            emailService.sendLoginOTP(email, otp, user.getFirstname());
//        } else {
//            throw new IllegalArgumentException("Invalid OTP type");
//        }
//
//        userRepository.save(user);
//
//        return AuthenticationResponse.builder()
//                .message("OTP resent successfully! Please check your email.")
//                .email(email)
//                .requiresOtp(true)
//                .build();
//    }
//}

package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.UserRepository;
import com.example.resultmanagementsystem.Dto.userdto.*;
import com.example.resultmanagementsystem.config.JWTService;
import com.example.resultmanagementsystem.model.Role.Role;
import com.example.resultmanagementsystem.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Create user without OTP
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .emailVerified(true) // Set to true since no OTP verification needed
                .isVerified(true)
                .departmentId(new HashSet<>())
                .build();

        userRepository.save(user);

        // Send welcome email directly
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstname());
        } catch (Exception e) {
            // Log error but don't fail the registration
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        // Generate JWT token immediately
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .message("Registration successful! Welcome to Result Management System.")
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .role(user.getRole().name())
                .departmentId(user.getDepartmentId())
                .requiresOtp(false)
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Validate request
            if (request == null || request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }

            System.out.println("Attempting to authenticate user with email: " + request.getEmail());

            // Find user by email only
            var user = userRepository.findByEmail(request.getEmail().trim())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

            System.out.println("User found: " + user.getEmail() + ", Role: " + user.getRole());

            // Generate JWT token immediately
            String jwtToken = jwtService.generateToken(user);
            System.out.println("JWT token generated successfully");

            // Get studentId if user is a student
            String studentId = null;
            if (user.getRole() == Role.STUDENT) {
                try {
                    System.out.println("User is a student, fetching student details...");
                    Object studentDetails = studentService.getStudentIdByEmail(user.getEmail());
                    if (studentDetails != null) {
                        studentId = studentDetails.toString();
                        System.out.println("Student ID found: " + studentId);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to get student ID: " + e.getMessage());
                    e.printStackTrace();
                    // Continue without student ID
                }
            }

            System.out.println("Building authentication response...");

            return AuthenticationResponse.builder()
                    .message("Login successful!")
                    .token(jwtToken)
                    .email(user.getEmail())
                    .firstName(user.getFirstname() != null ? user.getFirstname() : "")
                    .lastName(user.getLastname() != null ? user.getLastname() : "")
                    .role(user.getRole() != null ? user.getRole().name() : "UNKNOWN")
                    .departmentId(user.getDepartmentId() != null ? user.getDepartmentId() : new HashSet<>())
                    .studentId(studentId)
                    .requiresOtp(false)
                    .build();

        } catch (IllegalArgumentException e) {
            System.err.println("Authentication failed with IllegalArgumentException: " + e.getMessage());
            throw e; // Re-throw as it should be handled as 400/401
        } catch (Exception e) {
            System.err.println("Unexpected error during authentication: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Authentication failed due to server error: " + e.getMessage(), e);
        }
    }
}