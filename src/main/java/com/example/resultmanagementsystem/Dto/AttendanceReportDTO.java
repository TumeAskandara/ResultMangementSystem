package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceReportDTO {
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;
    private int totalClasses;
    private int present;
    private int absent;
    private int late;
    private int excused;
    private double percentage;
}
