package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "learning_paths")
public class LearningPath {
    @Id
    private String id = UUID.randomUUID().toString();

    private String title;
    private String description;
    private String departmentId;
    private List<String> courseIds;
    private String prerequisites;
    private Difficulty difficulty;
    private int estimatedDuration;
    @Builder.Default
    private Set<String> enrolledStudents = new HashSet<>();
    @Builder.Default
    private Set<String> completedStudents = new HashSet<>();
    private String createdBy;
    private boolean isPublished;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Difficulty {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
}
