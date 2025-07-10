package com.example.resultmanagementsystem.Dto.NotAndTimeTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// Enhanced Timetable DTOs
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimetableRequest {
    private String departmentId;
    private String teacherId;
    private String substituteTeacherId;
    private String semester;
    private String subject;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private String courseCode;
    private Integer credits;
    private String academicYear;
    private String section;
    private String sessionType;
    private String status;
    private String substituteReason;
    private Boolean isSubstituted;
}
