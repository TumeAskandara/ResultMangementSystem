package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Document(collection = "announcements")
public class Announcement {
    @Id
    private String id = UUID.randomUUID().toString();

    private String title;
    private String content;
    private AnnouncementType type;
    private AnnouncementPriority priority;
    private TargetAudience targetAudience;
    private String departmentId;
    private List<String> attachments;
    private String publishedBy;
    private String publishedByName;
    private LocalDateTime publishDate;
    private LocalDateTime expiryDate;
    private boolean isActive;
    private boolean isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum AnnouncementType {
        GENERAL, ACADEMIC, ADMINISTRATIVE, EMERGENCY, EVENT
    }

    public enum AnnouncementPriority {
        LOW, NORMAL, HIGH, URGENT
    }

    public enum TargetAudience {
        ALL, STUDENTS, TEACHERS, PARENTS, STAFF, DEPARTMENT
    }
}
