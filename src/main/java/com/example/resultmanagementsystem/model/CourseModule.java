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
@Document(collection = "course_modules")
public class CourseModule {
    @Id
    private String id = UUID.randomUUID().toString();
    private String courseId;
    private String title;
    private String description;
    private int orderIndex;
    private boolean isPublished;
    private String prerequisiteModuleId;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
