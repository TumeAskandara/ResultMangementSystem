package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "exams")
public class Exam {
    @Id
    private String examId = UUID.randomUUID().toString();
    private String title;
    private String description;
    private String courseId;
    private Set<String> departmentId;
    private String teacherId;
    private ExamType examType;
    private int totalMarks;
    private int passingMarks;
    private int duration;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String venue;
    private String academicYear;
    private String semester;
    private ExamStatus status;
    private String instructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum ExamType {
        MIDTERM, FINAL, QUIZ, PRACTICAL, ASSIGNMENT_EXAM, MOCK
    }

    public enum ExamStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, POSTPONED
    }
}
