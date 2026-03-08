package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "grade_appeals")
public class GradeAppeal {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String courseId;
    private String resultId;
    private String reason;
    private String currentGrade;
    private String requestedAction;
    private AppealStatus status = AppealStatus.PENDING;
    private String reviewedBy;
    private String reviewNotes;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;

    public enum AppealStatus {
        PENDING, UNDER_REVIEW, APPROVED, REJECTED
    }
}
