package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "attendance_summaries")
public class AttendanceSummary {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String courseId;
    private String academicYear;
    private String semester;
    private int totalClasses;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private int excusedCount;
    private double attendancePercentage;
    private LocalDateTime lastUpdated;
}
