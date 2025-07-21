package com.example.resultmanagementsystem.model;

import com.example.resultmanagementsystem.model.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id = UUID.randomUUID().toString();
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String password;

    // Original verification fields
    private String verificationToken;
    private boolean isVerified = false;

    // Student-specific fields
    private String studentId = UUID.randomUUID().toString();
    private String name;
    private Set<String> departmentId;

    // Email verification fields with OTP
    private boolean emailVerified = false;
    private String emailVerificationToken;
    private LocalDateTime emailVerificationTokenExpiry;

    // OTP fields for login
    private String loginOtp;
    private LocalDateTime loginOtpExpiry;
    private boolean loginOtpVerified = false;

    // OTP fields for registration
    private String registrationOtp;
    private LocalDateTime registrationOtpExpiry;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // We're using email as the username
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Use emailVerified for OTP-based verification, fallback to isVerified for compatibility
        return emailVerified || isVerified;
    }
}
