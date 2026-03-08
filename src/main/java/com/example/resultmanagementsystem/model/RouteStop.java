package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RouteStop {
    private String stopName;
    private int stopOrder;
    private String pickupTime;
    private double latitude;
    private double longitude;
}
