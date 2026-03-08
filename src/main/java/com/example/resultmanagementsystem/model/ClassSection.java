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
@Document(collection = "class_sections")
public class ClassSection {
    @Id
    private String id = UUID.randomUUID().toString();

    private String className;
    private String section;
    private String departmentId;
    private String academicYear;
    private String classTeacherId;
    private String classTeacherName;
    private int capacity;
    private int currentStrength;
    @Builder.Default
    private Set<String> studentIds = new HashSet<>();
    @Builder.Default
    private Set<String> courseIds = new HashSet<>();
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
