package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "enrollments")
public class Enrollment {

    @Id
    private String enrollmentId = UUID.randomUUID().toString();
    private String studentId;
    private String admissionApplicationId;
    private String departmentId;
    private String academicYear;
    private String semester;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private EnrollmentType enrollmentType;
    private String classId;
    private String section;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum EnrollmentStatus {
        ACTIVE, SUSPENDED, WITHDRAWN, GRADUATED, TRANSFERRED
    }

    public enum EnrollmentType {
        NEW_ADMISSION, RETURNING, TRANSFER
    }
}
