package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscussionForumDTO {
    private String courseId;
    private String title;
    private String description;
    private String createdBy;
    private boolean isPinned;
}
