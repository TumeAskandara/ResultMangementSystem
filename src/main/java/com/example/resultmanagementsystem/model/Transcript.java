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
@Document(collection = "transcripts")
public class Transcript {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String studentName;
    private String registrationNumber;
    private String departmentId;
    private String departmentName;
    private List<TranscriptSemester> semesters;
    private double cumulativeGPA;
    private double totalCreditsAttempted;
    private double totalCreditsEarned;
    private GraduationStatus graduationStatus;
    private LocalDateTime generatedAt;
    private String verificationCode = UUID.randomUUID().toString();

    public enum GraduationStatus {
        IN_PROGRESS, GRADUATED, WITHDRAWN
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TranscriptSemester {
        private String academicYear;
        private String semester;
        private List<TranscriptCourse> courses;
        private double semesterGPA;
        private double creditsAttempted;
        private double creditsEarned;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TranscriptCourse {
        private String courseCode;
        private String courseTitle;
        private double credits;
        private String grade;
        private double gradePoint;
    }
}
