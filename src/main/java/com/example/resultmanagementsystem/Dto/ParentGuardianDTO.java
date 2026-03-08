package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.ParentGuardian;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentGuardianDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    @NotNull(message = "Relationship is required")
    private ParentGuardian.Relationship relationship;

    private String occupation;

    private Set<String> studentIds;
}
