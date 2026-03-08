package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.TransportAssignmentDTO;
import com.example.resultmanagementsystem.Dto.TransportRouteDTO;
import com.example.resultmanagementsystem.Dto.TransportStatsDTO;
import com.example.resultmanagementsystem.Dto.VehicleDTO;
import com.example.resultmanagementsystem.Dto.Repository.TransportAssignmentRepository;
import com.example.resultmanagementsystem.Dto.Repository.TransportRouteRepository;
import com.example.resultmanagementsystem.Dto.Repository.VehicleRepository;
import com.example.resultmanagementsystem.model.RouteStop;
import com.example.resultmanagementsystem.model.TransportAssignment;
import com.example.resultmanagementsystem.model.TransportRoute;
import com.example.resultmanagementsystem.model.Vehicle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransportService {

    private final VehicleRepository vehicleRepository;
    private final TransportRouteRepository transportRouteRepository;
    private final TransportAssignmentRepository transportAssignmentRepository;

    // ==================== VEHICLE MANAGEMENT ====================

    public Vehicle addVehicle(VehicleDTO dto) {
        log.info("Adding new vehicle: {}", dto.getVehicleNumber());
        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID().toString())
                .vehicleNumber(dto.getVehicleNumber())
                .vehicleType(dto.getVehicleType())
                .capacity(dto.getCapacity())
                .driverName(dto.getDriverName())
                .driverPhone(dto.getDriverPhone())
                .driverLicenseNumber(dto.getDriverLicenseNumber())
                .routeId(dto.getRouteId())
                .status(dto.getStatus() != null ? dto.getStatus() : Vehicle.VehicleStatus.ACTIVE)
                .insuranceExpiry(dto.getInsuranceExpiry())
                .lastServiceDate(dto.getLastServiceDate())
                .gpsTrackerId(dto.getGpsTrackerId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    public Page<Vehicle> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    public Vehicle updateVehicle(String id, VehicleDTO dto) {
        Vehicle vehicle = getVehicleById(id);
        if (dto.getVehicleNumber() != null) vehicle.setVehicleNumber(dto.getVehicleNumber());
        if (dto.getVehicleType() != null) vehicle.setVehicleType(dto.getVehicleType());
        if (dto.getCapacity() > 0) vehicle.setCapacity(dto.getCapacity());
        if (dto.getDriverName() != null) vehicle.setDriverName(dto.getDriverName());
        if (dto.getDriverPhone() != null) vehicle.setDriverPhone(dto.getDriverPhone());
        if (dto.getDriverLicenseNumber() != null) vehicle.setDriverLicenseNumber(dto.getDriverLicenseNumber());
        if (dto.getRouteId() != null) vehicle.setRouteId(dto.getRouteId());
        if (dto.getStatus() != null) vehicle.setStatus(dto.getStatus());
        if (dto.getInsuranceExpiry() != null) vehicle.setInsuranceExpiry(dto.getInsuranceExpiry());
        if (dto.getLastServiceDate() != null) vehicle.setLastServiceDate(dto.getLastServiceDate());
        if (dto.getGpsTrackerId() != null) vehicle.setGpsTrackerId(dto.getGpsTrackerId());
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicleStatus(String id, Vehicle.VehicleStatus status) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setStatus(status);
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    // ==================== ROUTE MANAGEMENT ====================

    public TransportRoute createRoute(TransportRouteDTO dto) {
        log.info("Creating new route: {}", dto.getRouteName());
        TransportRoute route = TransportRoute.builder()
                .id(UUID.randomUUID().toString())
                .routeName(dto.getRouteName())
                .routeNumber(dto.getRouteNumber())
                .stops(dto.getStops() != null ? dto.getStops() : new ArrayList<>())
                .vehicleId(dto.getVehicleId())
                .estimatedDuration(dto.getEstimatedDuration())
                .distance(dto.getDistance())
                .isActive(dto.isActive())
                .fare(dto.getFare())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return transportRouteRepository.save(route);
    }

    public TransportRoute getRouteById(String id) {
        return transportRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport route not found with id: " + id));
    }

    public List<TransportRoute> getAllActiveRoutes() {
        return transportRouteRepository.findByIsActive(true);
    }

    public TransportRoute updateRoute(String id, TransportRouteDTO dto) {
        TransportRoute route = getRouteById(id);
        if (dto.getRouteName() != null) route.setRouteName(dto.getRouteName());
        if (dto.getRouteNumber() != null) route.setRouteNumber(dto.getRouteNumber());
        if (dto.getStops() != null) route.setStops(dto.getStops());
        if (dto.getVehicleId() != null) route.setVehicleId(dto.getVehicleId());
        if (dto.getEstimatedDuration() > 0) route.setEstimatedDuration(dto.getEstimatedDuration());
        if (dto.getDistance() > 0) route.setDistance(dto.getDistance());
        route.setActive(dto.isActive());
        if (dto.getFare() > 0) route.setFare(dto.getFare());
        route.setUpdatedAt(LocalDateTime.now());
        return transportRouteRepository.save(route);
    }

    public TransportRoute addStopToRoute(String routeId, RouteStop stop) {
        TransportRoute route = getRouteById(routeId);
        List<RouteStop> stops = route.getStops();
        if (stops == null) {
            stops = new ArrayList<>();
        }
        stops.add(stop);
        route.setStops(stops);
        route.setUpdatedAt(LocalDateTime.now());
        return transportRouteRepository.save(route);
    }

    public TransportRoute removeStopFromRoute(String routeId, String stopName) {
        TransportRoute route = getRouteById(routeId);
        List<RouteStop> stops = route.getStops();
        if (stops != null) {
            stops.removeIf(s -> s.getStopName().equalsIgnoreCase(stopName));
            route.setStops(stops);
        }
        route.setUpdatedAt(LocalDateTime.now());
        return transportRouteRepository.save(route);
    }

    // ==================== ASSIGNMENT MANAGEMENT ====================

    public TransportAssignment assignStudentToRoute(TransportAssignmentDTO dto) {
        log.info("Assigning student {} to route {}", dto.getStudentId(), dto.getRouteId());
        TransportAssignment assignment = TransportAssignment.builder()
                .id(UUID.randomUUID().toString())
                .studentId(dto.getStudentId())
                .studentName(dto.getStudentName())
                .routeId(dto.getRouteId())
                .routeName(dto.getRouteName())
                .stopName(dto.getStopName())
                .academicYear(dto.getAcademicYear())
                .status(TransportAssignment.TransportAssignmentStatus.ACTIVE)
                .assignedAt(LocalDateTime.now())
                .transportFee(dto.getTransportFee())
                .build();
        return transportAssignmentRepository.save(assignment);
    }

    public List<TransportAssignment> getAssignmentsByStudent(String studentId) {
        return transportAssignmentRepository.findByStudentId(studentId);
    }

    public List<TransportAssignment> getAssignmentsByRoute(String routeId) {
        return transportAssignmentRepository.findByRouteId(routeId);
    }

    public void removeStudentFromRoute(String assignmentId) {
        TransportAssignment assignment = transportAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Transport assignment not found with id: " + assignmentId));
        assignment.setStatus(TransportAssignment.TransportAssignmentStatus.INACTIVE);
        transportAssignmentRepository.save(assignment);
    }

    // ==================== STATISTICS ====================

    public TransportStatsDTO getTransportStatistics() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<TransportRoute> allRoutes = transportRouteRepository.findAll();
        List<TransportAssignment> allAssignments = transportAssignmentRepository.findAll();

        long activeVehicles = allVehicles.stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.ACTIVE).count();
        long maintenanceVehicles = allVehicles.stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.MAINTENANCE).count();
        long activeRoutes = allRoutes.stream().filter(TransportRoute::isActive).count();
        long activeAssignments = allAssignments.stream()
                .filter(a -> a.getStatus() == TransportAssignment.TransportAssignmentStatus.ACTIVE).count();
        int totalCapacity = allVehicles.stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.ACTIVE)
                .mapToInt(Vehicle::getCapacity).sum();
        double totalFare = allAssignments.stream()
                .filter(a -> a.getStatus() == TransportAssignment.TransportAssignmentStatus.ACTIVE)
                .mapToDouble(TransportAssignment::getTransportFee).sum();

        return TransportStatsDTO.builder()
                .totalVehicles(allVehicles.size())
                .activeVehicles(activeVehicles)
                .maintenanceVehicles(maintenanceVehicles)
                .totalRoutes(allRoutes.size())
                .activeRoutes(activeRoutes)
                .totalAssignments(allAssignments.size())
                .activeAssignments(activeAssignments)
                .totalCapacity(totalCapacity)
                .totalFareCollected(totalFare)
                .build();
    }
}
