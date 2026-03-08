package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Attendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Status is required")
    private Attendance.AttendanceStatus status;

    private String remarks;
}
