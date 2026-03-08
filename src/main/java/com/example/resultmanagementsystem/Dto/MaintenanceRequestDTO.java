package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.HostelMaintenanceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRequestDTO {
    private String id;
    private String roomId;
    private String roomNumber;
    private String hostelId;
    private String requestedBy;
    private String requestedByName;
    private HostelMaintenanceRequest.IssueType issueType;
    private String description;
    private HostelMaintenanceRequest.MaintenancePriority priority;
    private HostelMaintenanceRequest.MaintenanceStatus status;
    private String assignedTo;
}
