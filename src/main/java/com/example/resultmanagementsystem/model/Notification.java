package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String notificationId = UUID.randomUUID().toString();
    private String title;
    private String message;
    private String type; // SCHEDULE_CHANGE, DAILY_REMINDER, SUBSTITUTION, CANCELLATION
    private String priority; // HIGH, MEDIUM, LOW
    private List<String> recipientIds; // Teacher IDs, Student IDs, or Department IDs
    private String recipientType; // TEACHER, STUDENT, DEPARTMENT, ALL
    private String timetableId; // Reference to related timetable
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime scheduledDate; // When to send the notification
    private Boolean sent = false;
    private Boolean isRead = false;
    private String status = "PENDING"; // PENDING, SENT, FAILED, CANCELLED
    private  String departmentId;


}