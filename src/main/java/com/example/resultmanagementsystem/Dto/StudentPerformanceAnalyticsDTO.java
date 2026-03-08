package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentPerformanceAnalyticsDTO {
    private String studentId;
    private String studentName;
    private String departmentName;
    private Map<String, Double> semesterGPAs;
    private double cumulativeGPA;
    private double attendancePercentage;
    private int assignmentsCompleted;
    private int totalAssignments;
    private PerformanceTrend trend;

    public enum PerformanceTrend {
        IMPROVING, DECLINING, STABLE
    }
}
