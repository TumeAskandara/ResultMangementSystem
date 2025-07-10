package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "substitute_requests")
public class SubstituteRequest {
    @Id
    private String requestId = UUID.randomUUID().toString();
    private String originalTeacherId;
    private String substituteTeacherId;
    private String timetableId;
    private String reason;
    private LocalDate requestDate;
    private LocalDate substituteDate;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, COMPLETED
    private String approvedBy; // Admin/HOD ID
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private String comments;
}
