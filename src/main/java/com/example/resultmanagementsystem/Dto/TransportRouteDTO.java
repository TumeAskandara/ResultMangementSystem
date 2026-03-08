package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.RouteStop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportRouteDTO {
    private String id;
    private String routeName;
    private String routeNumber;
    private List<RouteStop> stops;
    private String vehicleId;
    private int estimatedDuration;
    private double distance;
    private boolean isActive;
    private double fare;
}
