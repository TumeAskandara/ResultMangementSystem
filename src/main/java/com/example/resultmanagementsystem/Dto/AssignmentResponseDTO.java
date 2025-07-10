package com.example.resultmanagementsystem.Dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssignmentResponseDTO {
    private String assignmentId;
    private String title;
    private String description;
    private String departmentName;
    private String teacherName;
    private LocalDateTime dueDate;
    private Integer maxPoints;
    private List<String> attachmentUrls;
    private LocalDateTime createdAt;
    private boolean isSubmitted;
    private boolean isOverdue;
    private String submissionStatus; // NOT_SUBMITTED, SUBMITTED, GRADED
}