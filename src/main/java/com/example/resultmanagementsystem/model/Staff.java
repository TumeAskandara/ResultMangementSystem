package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "staff")
public class Staff {

    @Id
    private String id = UUID.randomUUID().toString();

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private StaffType staffType;
    private String designation;
    private String departmentId;
    private List<String> qualifications;
    private LocalDate dateOfJoining;
    private EmploymentStatus employmentStatus;
    private double salary;
    private String bankAccountNumber;
    private String bankName;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String profileImageUrl;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum StaffType {
        TEACHING, NON_TEACHING, ADMINISTRATIVE
    }

    public enum EmploymentStatus {
        ACTIVE, ON_LEAVE, SUSPENDED, TERMINATED, RETIRED
    }
}
