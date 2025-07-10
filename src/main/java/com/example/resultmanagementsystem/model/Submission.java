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
@Document(collection = "submissions")
public class Submission {
    @Id
    private String submissionId = UUID.randomUUID().toString();
    private String assignmentId;
    private String studentId;
    private String studentName;
    private List<String> fileUrls;
    private String comments;
    private LocalDateTime submittedAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private Integer grade;
    private String feedback;
    private boolean isLate;
    private String gradedByName;
    private String gradedBy;

}