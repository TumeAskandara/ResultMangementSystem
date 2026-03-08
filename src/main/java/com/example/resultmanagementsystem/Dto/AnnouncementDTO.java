package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Announcement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementDTO {
    private String id;
    private String title;
    private String content;
    private Announcement.AnnouncementType type;
    private Announcement.AnnouncementPriority priority;
    private Announcement.TargetAudience targetAudience;
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
}
