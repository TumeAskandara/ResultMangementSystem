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
@Document(collection = "exam_results")
public class ExamResult {
    @Id
    private String id = UUID.randomUUID().toString();
    private String examId;
    private String studentId;
    private List<StudentAnswer> answers;
    private double totalScore;
    private double percentage;
    private String grade;
    private ExamResultStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private String gradedBy;
    private LocalDateTime gradedAt;
    private String feedback;

    public enum ExamResultStatus {
        PASSED, FAILED, ABSENT, DISQUALIFIED
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class StudentAnswer {
        private String questionId;
        private String answer;
        private double marksObtained;
        private String feedback;
    }
}
