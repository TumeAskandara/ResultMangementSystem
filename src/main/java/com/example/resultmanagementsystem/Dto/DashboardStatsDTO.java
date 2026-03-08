package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardStatsDTO {
    private long totalStudents;
    private long totalTeachers;
    private long totalCourses;
    private long totalDepartments;
    private long totalStaff;
    private long activeEnrollments;
    private long pendingAdmissions;
    private long pendingComplaints;
    private long pendingLeaves;
    private long overdueBooks;
    private long pendingMaintenance;
    private double feeCollectionRate;
    private double averageAttendance;
    private double overallPassRate;
}
