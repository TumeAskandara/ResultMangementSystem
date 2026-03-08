package com.example.resultmanagementsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportCardRequestDTO {
    @NotBlank(message = "Student ID is required")
    private String studentId;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    @NotBlank(message = "Semester is required")
    private String semester;
}
