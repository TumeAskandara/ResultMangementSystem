package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Exam;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamDTO {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotBlank(message = "Course ID is required")
    private String courseId;
    private Set<String> departmentId;
    @NotBlank(message = "Teacher ID is required")
    private String teacherId;
    @NotNull(message = "Exam type is required")
    private Exam.ExamType examType;
    @Min(value = 1, message = "Total marks must be at least 1")
    private int totalMarks;
    @Min(value = 0, message = "Passing marks cannot be negative")
    private int passingMarks;
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int duration;
    @NotNull(message = "Start date time is required")
    private LocalDateTime startDateTime;
    @NotNull(message = "End date time is required")
    private LocalDateTime endDateTime;
    private String venue;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    @NotBlank(message = "Semester is required")
    private String semester;
    private String instructions;
}
