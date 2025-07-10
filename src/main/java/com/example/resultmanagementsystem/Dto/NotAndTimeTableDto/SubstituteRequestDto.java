package com.example.resultmanagementsystem.Dto.NotAndTimeTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubstituteRequestDto {
    private String originalTeacherId;
    private String originalTeacherName;
    private String substituteTeacherId;
    private String substituteTeacherName;
    private String timetableId;
    private String subject;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private String reason;
    private LocalDate requestDate;
    private LocalDate substituteDate;
    private String status;
    private String approvedBy;
    private String comments;
}