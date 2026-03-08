package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.LearningPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LearningPathDTO {
    private String id;
    private String title;
    private String description;
    private String departmentId;
    private List<String> courseIds;
    private String prerequisites;
    private LearningPath.Difficulty difficulty;
    private int estimatedDuration;
    private Set<String> enrolledStudents;
    private Set<String> completedStudents;
    private String createdBy;
    private boolean isPublished;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
