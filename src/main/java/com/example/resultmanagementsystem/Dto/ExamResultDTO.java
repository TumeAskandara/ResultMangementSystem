package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.ExamResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamResultDTO {
    @NotBlank(message = "Exam ID is required")
    private String examId;
    @NotBlank(message = "Student ID is required")
    private String studentId;
    @NotNull(message = "Answers are required")
    private List<ExamResult.StudentAnswer> answers;
    private double totalScore;
    private double percentage;
    private String grade;
    private ExamResult.ExamResultStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private String feedback;
}
