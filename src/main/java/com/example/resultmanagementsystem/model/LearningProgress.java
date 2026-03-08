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
@Document(collection = "learning_progress")
public class LearningProgress {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String courseId;
    private String moduleId;
    private String lessonId;
    private ProgressStatus status = ProgressStatus.NOT_STARTED;
    private double progressPercentage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime lastAccessedAt;
    private int timeSpentMinutes;

    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }
}
