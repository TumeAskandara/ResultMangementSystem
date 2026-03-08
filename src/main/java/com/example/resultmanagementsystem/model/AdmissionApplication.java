package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "admission_applications")
public class AdmissionApplication {

    @Id
    private String id = UUID.randomUUID().toString();
    private String applicantFirstName;
    private String applicantLastName;
    private String applicantEmail;
    private String applicantPhone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private String address;
    private String previousSchool;
    private String previousGrade;
    private String departmentId;
    private String academicYear;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;
    private String reviewedBy;
    private String reviewNotes;
    private LocalDateTime reviewDate;
    private List<AdmissionDocument> documents;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String parentGuardianName;
    private String parentGuardianEmail;
    private String parentGuardianPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum ApplicationStatus {
        PENDING, UNDER_REVIEW, ACCEPTED, REJECTED, WAITLISTED, ENROLLED
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class AdmissionDocument {
        private String documentName;
        private String documentUrl;
        private String documentType;
        private LocalDateTime uploadedAt;
    }
}
