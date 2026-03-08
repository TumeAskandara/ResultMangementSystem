package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "transport_routes")
public class TransportRoute {
    @Id
    private String id = UUID.randomUUID().toString();
    private String routeName;
    private String routeNumber;
    @Builder.Default
    private List<RouteStop> stops = new ArrayList<>();
    private String vehicleId;
    private int estimatedDuration;
    private double distance;
    private boolean isActive;
    private double fare;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
