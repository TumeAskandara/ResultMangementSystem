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
@Document(collection = "lessons")
public class Lesson {
    @Id
    private String id = UUID.randomUUID().toString();
    private String moduleId;
    private String courseId;
    private String title;
    private ContentType contentType;
    private String content;
    private String videoUrl;
    private int duration;
    private int orderIndex;
    private boolean isPublished;
    private List<LessonAttachment> attachments;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum ContentType {
        TEXT, VIDEO, PDF, PRESENTATION, LINK, SCORM
    }
}
