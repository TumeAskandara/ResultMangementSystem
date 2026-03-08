package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClassStatsDTO {
    private String classId;
    private String className;
    private String section;
    private int capacity;
    private int currentStrength;
    private int availableSlots;
    private int totalCourses;
    private double averageAttendance;
    private double averageGPA;
    private String classTeacherName;
}
