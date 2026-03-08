package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportStatsDTO {
    private long totalVehicles;
    private long activeVehicles;
    private long maintenanceVehicles;
    private long totalRoutes;
    private long activeRoutes;
    private long totalAssignments;
    private long activeAssignments;
    private int totalCapacity;
    private double totalFareCollected;
}
