package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private String id;
    private String vehicleNumber;
    private Vehicle.VehicleType vehicleType;
    private int capacity;
    private String driverName;
    private String driverPhone;
    private String driverLicenseNumber;
    private String routeId;
    private Vehicle.VehicleStatus status;
    private LocalDate insuranceExpiry;
    private LocalDate lastServiceDate;
    private String gpsTrackerId;
}
