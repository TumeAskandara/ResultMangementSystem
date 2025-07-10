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
@Document(collection = "notification_logs")
public class NotificationLog {
    @Id
    private String notificationId = UUID.randomUUID().toString();
    private String eventId;
    private List<String> recipientStudentIds;
    private String message;
    private NotificationType type;
    private LocalDateTime sentAt = LocalDateTime.now();
    private boolean successful = true;
    private String errorMessage;

    public enum NotificationType {
        EMAIL, SMS, IN_APP, PUSH
    }
}