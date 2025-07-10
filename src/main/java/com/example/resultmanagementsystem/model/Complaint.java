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
@Document(collection = "complaints")
@Data
public class Complaint {
    @Id
    private String id = UUID.randomUUID().toString();

    // Reference to the student who made the complaint
    private String studentId;



    // Reference to the course the complaint is about
    private String courseId;

    // Reference to the relevant result if applicable
    private String resultId;

    // Reference to the teacher/admin assigned to handle this complaint
    private String assignedToId;

    // Complaint details
    private String description;

    // Tracking status of the complaint
    private ComplaintStatus status = ComplaintStatus.PENDING;

    // Admin/instructor response to the complaint
    private String response;

    private String triggerReason;

    // Timestamps
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;

    // Additional fields for notifications
    private boolean studentNotified = false;
    private boolean teacherNotified = false;
    private LocalDateTime lastNotificationSent;

    // Enum for status to replace String literals
    public enum ComplaintStatus {
        PENDING, UNDER_REVIEW, RESOLVED, REJECTED
    }
}