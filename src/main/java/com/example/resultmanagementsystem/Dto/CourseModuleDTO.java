package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseModuleDTO {
    private String courseId;
    private String title;
    private String description;
    private int orderIndex;
    private boolean isPublished;
    private String prerequisiteModuleId;
    private String createdBy;
}
