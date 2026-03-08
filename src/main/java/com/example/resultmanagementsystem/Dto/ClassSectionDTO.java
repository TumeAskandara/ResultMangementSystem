package com.example.resultmanagementsystem.Dto;

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
public class ClassSectionDTO {
    private String id;
    private String className;
    private String section;
    private String departmentId;
    private String academicYear;
    private String classTeacherId;
    private String classTeacherName;
    private int capacity;
    private int currentStrength;
    private Set<String> studentIds;
    private Set<String> courseIds;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
