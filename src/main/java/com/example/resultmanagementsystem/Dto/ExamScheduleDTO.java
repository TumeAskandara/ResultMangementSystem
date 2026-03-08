package com.example.resultmanagementsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamScheduleDTO {
    @NotBlank(message = "Exam ID is required")
    private String examId;
    @NotBlank(message = "Department ID is required")
    private String departmentId;
    private String venue;
    private String invigilatorId;
    @NotNull(message = "Date is required")
    private LocalDate date;
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    @NotBlank(message = "Semester is required")
    private String semester;
    private String status;
}
