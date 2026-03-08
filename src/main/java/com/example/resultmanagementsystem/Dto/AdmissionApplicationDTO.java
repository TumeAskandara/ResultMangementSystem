package com.example.resultmanagementsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdmissionApplicationDTO {

    @NotBlank(message = "Applicant first name is required")
    private String applicantFirstName;

    @NotBlank(message = "Applicant last name is required")
    private String applicantLastName;

    @NotBlank(message = "Applicant email is required")
    private String applicantEmail;

    @NotBlank(message = "Applicant phone is required")
    private String applicantPhone;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Previous school is required")
    private String previousSchool;

    @NotBlank(message = "Previous grade is required")
    private String previousGrade;

    @NotBlank(message = "Department ID is required")
    private String departmentId;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotBlank(message = "Emergency contact name is required")
    private String emergencyContactName;

    @NotBlank(message = "Emergency contact phone is required")
    private String emergencyContactPhone;

    @NotBlank(message = "Parent/guardian name is required")
    private String parentGuardianName;

    @NotBlank(message = "Parent/guardian email is required")
    private String parentGuardianEmail;

    @NotBlank(message = "Parent/guardian phone is required")
    private String parentGuardianPhone;
}
