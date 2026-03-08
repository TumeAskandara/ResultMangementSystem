package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DepartmentAnalyticsDTO {
    private String departmentId;
    private String departmentName;
    private long totalStudents;
    private double averageGPA;
    private double attendanceRate;
    private double passRate;
    private List<String> topPerformers;
    private long enrollmentCount;
}
