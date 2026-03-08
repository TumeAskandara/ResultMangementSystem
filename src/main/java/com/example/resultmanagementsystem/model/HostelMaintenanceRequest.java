package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "hostel_maintenance_requests")
public class HostelMaintenanceRequest {
    @Id
    private String id = UUID.randomUUID().toString();
    private String roomId;
    private String roomNumber;
    private String hostelId;
    private String requestedBy;
    private String requestedByName;
    private IssueType issueType;
    private String description;
    private MaintenancePriority priority;
    private MaintenanceStatus status;
    private String assignedTo;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum IssueType {
        ELECTRICAL, PLUMBING, FURNITURE, CLEANING, OTHER
    }

    public enum MaintenancePriority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum MaintenanceStatus {
        PENDING, IN_PROGRESS, COMPLETED, REJECTED
    }
}
