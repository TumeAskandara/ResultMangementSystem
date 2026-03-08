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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id = UUID.randomUUID().toString();
    private String vehicleNumber;
    private VehicleType vehicleType;
    private int capacity;
    private String driverName;
    private String driverPhone;
    private String driverLicenseNumber;
    private String routeId;
    private VehicleStatus status;
    private LocalDate insuranceExpiry;
    private LocalDate lastServiceDate;
    private String gpsTrackerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum VehicleType {
        BUS, MINIBUS, VAN
    }

    public enum VehicleStatus {
        ACTIVE, MAINTENANCE, RETIRED
    }
}
