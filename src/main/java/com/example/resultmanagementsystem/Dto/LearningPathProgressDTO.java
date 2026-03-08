package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.LearningPathProgress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LearningPathProgressDTO {
    private String id;
    private String learningPathId;
    private String studentId;
    private int currentCourseIndex;
    private Set<String> completedCourseIds;
    private double progressPercentage;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
    private LearningPathProgress.ProgressStatus status;
}
