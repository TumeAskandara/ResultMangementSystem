package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "calendar_events")
public class CalendarEvent {
    @Id
    private String eventId = UUID.randomUUID().toString();
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String departmentId; // null for all departments
    private List<String> targetDepartments; // specific departments if not all
    private EventType eventType;
    private String createdBy; // admin ID
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private boolean notificationSent = false;
    private EventStatus status = EventStatus.ACTIVE;
    private String location;
    private boolean isRecurring = false;
    private RecurrencePattern recurrencePattern;

    public enum EventType {
        EXAM, LECTURE, HOLIDAY, MEETING, DEADLINE, WORKSHOP, SEMINAR, OTHER
    }

    public enum EventStatus {
        ACTIVE, CANCELLED, POSTPONED, COMPLETED
    }
}
