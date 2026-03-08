package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildSummaryDTO {
    private String studentId;
    private String studentName;
    private String departmentName;
    private double attendancePercentage;
    private double cgpa;
    private double outstandingFees;
    private List<Map<String, Object>> recentResults;
    private List<Map<String, Object>> upcomingEvents;
}
