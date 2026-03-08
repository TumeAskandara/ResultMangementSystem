package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "learning_path_progress")
public class LearningPathProgress {
    @Id
    private String id = UUID.randomUUID().toString();

    private String learningPathId;
    private String studentId;
    private int currentCourseIndex;
    @Builder.Default
    private Set<String> completedCourseIds = new HashSet<>();
    private double progressPercentage;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
    private ProgressStatus status;

    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, PAUSED
    }
}
