package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "attendance")
public class Attendance {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String classId;
    private String departmentId;
    private String courseId;
    private String teacherId;
    private LocalDate date;
    private AttendanceStatus status;
    private String markedBy;
    private LocalDateTime markedAt;
    private String remarks;
    private String academicYear;
    private String semester;
    private SessionType sessionType;

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }

    public enum SessionType {
        LECTURE, LAB, TUTORIAL
    }
}
