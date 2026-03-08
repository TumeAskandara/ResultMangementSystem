package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Attendance;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkAttendanceDTO {

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Attendance.SessionType sessionType;

    @NotEmpty(message = "Attendance records cannot be empty")
    @Valid
    private List<AttendanceRecordDTO> records;
}
