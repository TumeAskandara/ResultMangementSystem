package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "analytics_reports")
public class AnalyticsReport {
    @Id
    private String id = UUID.randomUUID().toString();

    private ReportType reportType;
    private String title;
    private Map<String, String> parameters;
    private Map<String, Object> data;
    private String generatedBy;
    private LocalDateTime generatedAt;
    private String academicYear;
    private String semester;

    public enum ReportType {
        STUDENT_PERFORMANCE,
        ATTENDANCE_TREND,
        FEE_COLLECTION,
        ENROLLMENT_TREND,
        TEACHER_PERFORMANCE,
        DEPARTMENT_COMPARISON,
        EXAM_ANALYSIS
    }
}
