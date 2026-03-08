package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelStatsDTO {
    private long totalHostels;
    private long activeHostels;
    private long totalRooms;
    private long availableRooms;
    private long totalCapacity;
    private long occupiedBeds;
    private long totalAllocations;
    private long activeAllocations;
    private long pendingMaintenanceRequests;
    private double occupancyRate;
}
