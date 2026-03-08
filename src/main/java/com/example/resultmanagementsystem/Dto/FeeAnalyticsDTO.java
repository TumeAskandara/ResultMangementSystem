package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeeAnalyticsDTO {
    private double totalExpected;
    private double totalCollected;
    private double totalOutstanding;
    private double collectionRate;
    private Map<String, Double> departmentBreakdown;
}
