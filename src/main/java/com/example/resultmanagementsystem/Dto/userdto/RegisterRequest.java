package com.example.resultmanagementsystem.Dto.userdto;

import com.example.resultmanagementsystem.model.Role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    private String password;
    private String role;


}