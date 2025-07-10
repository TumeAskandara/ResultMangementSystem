package com.example.resultmanagementsystem.Dto.NotAndTimeTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimetableResponse {
    private String timetableId;
    private String departmentId;
    private String teacherId;
    private String teacherName;
    private String substituteTeacherId;
    private String substituteTeacherName;
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
    private LocalDate substitutionDate;
    private Boolean isSubstituted;
    private String createdDate;
    private String updatedDate;
}
