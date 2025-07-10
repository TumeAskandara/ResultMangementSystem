package com.example.resultmanagementsystem.Dto.complainDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDashboardDTO {
    private long totalComplaints;
    private long pendingComplaints;
    private long underReviewComplaints;
    private long resolvedComplaints;
    private long rejectedComplaints;
    private double averageResolutionTimeInHours;
    private long complaintsLastWeek;
    private long complaintsLastMonth;
}