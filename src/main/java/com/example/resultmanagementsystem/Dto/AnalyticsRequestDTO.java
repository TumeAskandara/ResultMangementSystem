package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.AnalyticsReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalyticsRequestDTO {
    private AnalyticsReport.ReportType reportType;
    private String academicYear;
    private String semester;
    private String departmentId;
    private String courseId;
    private String studentId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
