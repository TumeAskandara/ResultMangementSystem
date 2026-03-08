package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "leave_requests")
public class LeaveRequest {

    @Id
    private String id = UUID.randomUUID().toString();

    private String staffId;
    private String staffName;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalDays;
    private String reason;
    private LeaveStatus status;
    private String approvedBy;
    private String approverRemarks;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private String attachmentUrl;

    public enum LeaveType {
        SICK, CASUAL, ANNUAL, MATERNITY, PATERNITY, UNPAID, STUDY, COMPASSIONATE
    }

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
}
