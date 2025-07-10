package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.TeacherRepository;
import com.example.resultmanagementsystem.model.Teacher;
import com.example.resultmanagementsystem.config.JWTService; // Assuming you have a JWT service
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherAuthService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final EmailService emailService;

    public Teacher registerTeacher(Teacher teacher) {
        // Check if email already exists
        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();

        // Encode password
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setVerified(false);

        // Save teacher
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Send verification email
        emailService.sendVerificationEmail(savedTeacher.getEmail(), verificationToken);

        return savedTeacher;
    }

    public String verifyTeacherEmail(String token) {
        // Implement email verification logic similar to User verification
        // This would require adding a method to TeacherRepository to find by verification token
        return "Teacher email verified successfully";
    }

    public String authenticateTeacher(String email, String password) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Teacher not found"));

        // Check if teacher is verified
        if (!teacher.isVerified()) {
            throw new IllegalStateException("Email not verified");
        }

        // Verify password
        if (!passwordEncoder.matches(password, teacher.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Generate and return JWT token
        return jwtService.generateToken((UserDetails) teacher);
    }
}