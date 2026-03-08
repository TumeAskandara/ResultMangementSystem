package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.QuestionBank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionBankDTO {
    @NotBlank(message = "Course ID is required")
    private String courseId;
    @NotBlank(message = "Teacher ID is required")
    private String teacherId;
    @NotBlank(message = "Question is required")
    private String question;
    private List<String> options;
    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;
    @NotNull(message = "Question type is required")
    private QuestionBank.QuestionType questionType;
    @NotNull(message = "Difficulty is required")
    private QuestionBank.Difficulty difficulty;
    @Min(value = 1, message = "Marks must be at least 1")
    private int marks;
    private String topic;
    private String explanation;
}
