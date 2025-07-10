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
@Document(collection = "assignments")
public class Assignment {
    @Id
    private String assignmentId = UUID.randomUUID().toString();
    private String title;
    private String description;
    private Set<String> departmentId;
    private String teacherId;
    private String teacherName;
    private LocalDateTime dueDate;
    private Integer maxPoints;
    private List<String> attachmentUrls;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private boolean isActive = true;
}
