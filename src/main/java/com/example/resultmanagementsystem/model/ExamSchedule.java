package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "exam_schedules")
public class ExamSchedule {
    @Id
    private String id = UUID.randomUUID().toString();
    private String examId;
    private String departmentId;
    private String venue;
    private String invigilatorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String academicYear;
    private String semester;
    private String status;
}
