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
@Document(collection = "timetables")
public class Timetable {
    @Id
    private String timetableId = UUID.randomUUID().toString();
    private String departmentId;
    private String teacherId;
    private String substituteTeacherId; // NEW: For substitute management
    private String semester;
    private String subject;
    private String dayOfWeek; // MONDAY, TUESDAY, etc.
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private String courseCode;
    private Integer credits;
    private String academicYear; // e.g., "2024-2025"
    private String section; // A, B, C, etc.
    private String sessionType; // LECTURE, LAB, TUTORIAL
    private LocalDate createdDate = LocalDate.now();
    private LocalDate updatedDate = LocalDate.now();
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, CANCELLED, SUBSTITUTED
    private String substituteReason; // NEW: Reason for substitution
    private LocalDate substitutionDate; // NEW: Date when substitution was made
    private Boolean isSubstituted = false; // NEW: Flag for substitution
    private Boolean notificationSent = false; // NEW: Flag for notification tracking
}
