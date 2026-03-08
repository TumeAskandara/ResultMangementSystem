package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Lesson;
import com.example.resultmanagementsystem.model.LessonAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonDTO {
    private String moduleId;
    private String courseId;
    private String title;
    private Lesson.ContentType contentType;
    private String content;
    private String videoUrl;
    private int duration;
    private int orderIndex;
    private boolean isPublished;
    private List<LessonAttachment> attachments;
    private String createdBy;
}
