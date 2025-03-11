package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.UserRepository;
import com.example.resultmanagementsystem.Dto.userdto.AuthenticationRequest;
import com.example.resultmanagementsystem.Dto.userdto.AuthenticationResponse;
import com.example.resultmanagementsystem.Dto.userdto.RegisterRequest;
import com.example.resultmanagementsystem.config.JWTService;
import com.example.resultmanagementsystem.model.Role.Role;
import com.example.resultmanagementsystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        // Generate a verification token
        String verificationToken = UUID.randomUUID().toString();

        // Create a new user with verification token
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.USER)

                .verificationToken(verificationToken)
                .isVerified(false) // Default to false
                .build();

        // Save the user
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return AuthenticationResponse.builder()
                .message("User registered. Check your email for verification.")
                .build();
    }

    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setVerified(true);
        user.setVerificationToken(null); // Remove the token after verification
        userRepository.save(user);

        return "Email verified successfully!";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Find the user by email
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // Generate JWT token
        var token = jwtService.generateToken(user);

        // Return the token and role in the response
        return AuthenticationResponse.builder()
                .token(token)
                .role(user.getRole().name())  // Convert enum to string
                .build();
    }

}
