package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Staff;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Staff information")
public class StaffDTO {

    @Schema(description = "First name of the staff member", example = "John")
    private String firstName;

    @Schema(description = "Last name of the staff member", example = "Doe")
    private String lastName;

    @Schema(description = "Email address", example = "john.doe@school.edu")
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Residential address", example = "123 Main Street")
    private String address;

    @Schema(description = "Date of birth", example = "1985-06-15")
    private LocalDate dateOfBirth;

    @Schema(description = "Gender", example = "Male")
    private String gender;

    @Schema(description = "Nationality", example = "American")
    private String nationality;

    @Schema(description = "Type of staff", example = "TEACHING")
    private Staff.StaffType staffType;

    @Schema(description = "Job designation", example = "Senior Teacher")
    private String designation;

    @Schema(description = "Department ID", example = "DEPT001")
    private String departmentId;

    @Schema(description = "List of qualifications")
    private List<String> qualifications;

    @Schema(description = "Date of joining", example = "2020-01-15")
    private LocalDate dateOfJoining;

    @Schema(description = "Employment status", example = "ACTIVE")
    private Staff.EmploymentStatus employmentStatus;

    @Schema(description = "Salary amount", example = "50000.00")
    private double salary;

    @Schema(description = "Bank account number", example = "1234567890")
    private String bankAccountNumber;

    @Schema(description = "Bank name", example = "National Bank")
    private String bankName;

    @Schema(description = "Emergency contact name", example = "Jane Doe")
    private String emergencyContactName;

    @Schema(description = "Emergency contact phone", example = "+0987654321")
    private String emergencyContactPhone;

    @Schema(description = "Profile image URL")
    private String profileImageUrl;

    @Schema(description = "Associated user ID")
    private String userId;
}
