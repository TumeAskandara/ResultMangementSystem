package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Attendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Status is required")
    private Attendance.AttendanceStatus status;

    private String remarks;

    private Attendance.SessionType sessionType;
}
