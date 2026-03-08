package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "report_cards")
public class ReportCard {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String studentName;
    private String departmentId;
    private String departmentName;
    private String academicYear;
    private String semester;
    private List<ReportCardEntry> results;
    private double semesterGPA;
    private double cumulativeGPA;
    private double totalCredits;
    private double totalCreditsEarned;
    private int classRank;
    private int totalStudentsInClass;
    private String remarks;
    private String generatedBy;
    private LocalDateTime generatedAt;
    private ReportCardStatus status;

    public enum ReportCardStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ReportCardEntry {
        private String courseCode;
        private String courseTitle;
        private double credits;
        private double ca;
        private double exams;
        private double total;
        private String grade;
        private double gradePoint;
        private String status;
    }
}
