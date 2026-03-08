package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.TransportAssignmentDTO;
import com.example.resultmanagementsystem.Dto.TransportRouteDTO;
import com.example.resultmanagementsystem.Dto.TransportStatsDTO;
import com.example.resultmanagementsystem.Dto.VehicleDTO;
import com.example.resultmanagementsystem.model.RouteStop;
import com.example.resultmanagementsystem.model.TransportAssignment;
import com.example.resultmanagementsystem.model.TransportRoute;
import com.example.resultmanagementsystem.model.Vehicle;
import com.example.resultmanagementsystem.services.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transport")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Transport Management", description = "APIs for managing school transport vehicles, routes, and student assignments")
public class TransportController {

    private final TransportService transportService;

    // ==================== VEHICLE ENDPOINTS ====================

    @Operation(summary = "Add a new vehicle", description = "Registers a new vehicle in the transport system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully",
                    content = @Content(schema = @Schema(implementation = Vehicle.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle data")
    })
    @PostMapping("/admin/vehicles")
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        log.info("Adding new vehicle: {}", vehicleDTO.getVehicleNumber());
        Vehicle vehicle = transportService.addVehicle(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @Operation(summary = "Get vehicle by ID", description = "Retrieves a specific vehicle by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = Vehicle.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> getVehicleById(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable String id) {
        Vehicle vehicle = transportService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @Operation(summary = "Get all vehicles (paginated)", description = "Retrieves all vehicles with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully")
    })
    @GetMapping("/vehicles")
    public ResponseEntity<Page<Vehicle>> getAllVehicles(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehicles = transportService.getAllVehicles(pageable);
        return ResponseEntity.ok(vehicles);
    }

    @Operation(summary = "Update a vehicle", description = "Updates an existing vehicle's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/admin/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable String id,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        Vehicle vehicle = transportService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok(vehicle);
    }

    @Operation(summary = "Update vehicle status", description = "Changes the status of a vehicle (ACTIVE, MAINTENANCE, RETIRED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PatchMapping("/admin/vehicles/{id}/status")
    public ResponseEntity<Vehicle> updateVehicleStatus(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable String id,
            @RequestBody Map<String, String> statusMap) {
        Vehicle.VehicleStatus status = Vehicle.VehicleStatus.valueOf(statusMap.get("status"));
        Vehicle vehicle = transportService.updateVehicleStatus(id, status);
        return ResponseEntity.ok(vehicle);
    }

    // ==================== ROUTE ENDPOINTS ====================

    @Operation(summary = "Create a new route", description = "Creates a new transport route with stops")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created successfully",
                    content = @Content(schema = @Schema(implementation = TransportRoute.class))),
            @ApiResponse(responseCode = "400", description = "Invalid route data")
    })
    @PostMapping("/admin/routes")
    public ResponseEntity<TransportRoute> createRoute(@Valid @RequestBody TransportRouteDTO routeDTO) {
        log.info("Creating new route: {}", routeDTO.getRouteName());
        TransportRoute route = transportService.createRoute(routeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(route);
    }

    @Operation(summary = "Get route by ID", description = "Retrieves a specific transport route by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route found"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @GetMapping("/routes/{id}")
    public ResponseEntity<TransportRoute> getRouteById(
            @Parameter(description = "Route ID", required = true) @PathVariable String id) {
        TransportRoute route = transportService.getRouteById(id);
        return ResponseEntity.ok(route);
    }

    @Operation(summary = "Get all active routes", description = "Retrieves all currently active transport routes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active routes retrieved successfully")
    })
    @GetMapping("/routes/active")
    public ResponseEntity<List<TransportRoute>> getAllActiveRoutes() {
        List<TransportRoute> routes = transportService.getAllActiveRoutes();
        return ResponseEntity.ok(routes);
    }

    @Operation(summary = "Update a route", description = "Updates an existing transport route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route updated successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @PutMapping("/admin/routes/{id}")
    public ResponseEntity<TransportRoute> updateRoute(
            @Parameter(description = "Route ID", required = true) @PathVariable String id,
            @Valid @RequestBody TransportRouteDTO routeDTO) {
        TransportRoute route = transportService.updateRoute(id, routeDTO);
        return ResponseEntity.ok(route);
    }

    @Operation(summary = "Add stop to route", description = "Adds a new stop to an existing transport route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stop added successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @PostMapping("/admin/routes/{routeId}/stops")
    public ResponseEntity<TransportRoute> addStopToRoute(
            @Parameter(description = "Route ID", required = true) @PathVariable String routeId,
            @Valid @RequestBody RouteStop stop) {
        TransportRoute route = transportService.addStopToRoute(routeId, stop);
        return ResponseEntity.ok(route);
    }

    @Operation(summary = "Remove stop from route", description = "Removes a stop from a transport route by stop name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stop removed successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @DeleteMapping("/admin/routes/{routeId}/stops/{stopName}")
    public ResponseEntity<TransportRoute> removeStopFromRoute(
            @Parameter(description = "Route ID", required = true) @PathVariable String routeId,
            @Parameter(description = "Stop name to remove", required = true) @PathVariable String stopName) {
        TransportRoute route = transportService.removeStopFromRoute(routeId, stopName);
        return ResponseEntity.ok(route);
    }

    // ==================== ASSIGNMENT ENDPOINTS ====================

    @Operation(summary = "Assign student to route", description = "Creates a new transport assignment for a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student assigned successfully",
                    content = @Content(schema = @Schema(implementation = TransportAssignment.class))),
            @ApiResponse(responseCode = "400", description = "Invalid assignment data")
    })
    @PostMapping("/assignments")
    public ResponseEntity<TransportAssignment> assignStudentToRoute(
            @Valid @RequestBody TransportAssignmentDTO assignmentDTO) {
        TransportAssignment assignment = transportService.assignStudentToRoute(assignmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
    }

    @Operation(summary = "Get assignments by student", description = "Retrieves all transport assignments for a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    })
    @GetMapping("/assignments/student/{studentId}")
    public ResponseEntity<List<TransportAssignment>> getAssignmentsByStudent(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId) {
        List<TransportAssignment> assignments = transportService.getAssignmentsByStudent(studentId);
        return ResponseEntity.ok(assignments);
    }

    @Operation(summary = "Get assignments by route", description = "Retrieves all student assignments for a specific route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    })
    @GetMapping("/assignments/route/{routeId}")
    public ResponseEntity<List<TransportAssignment>> getAssignmentsByRoute(
            @Parameter(description = "Route ID", required = true) @PathVariable String routeId) {
        List<TransportAssignment> assignments = transportService.getAssignmentsByRoute(routeId);
        return ResponseEntity.ok(assignments);
    }

    @Operation(summary = "Remove student from route", description = "Deactivates a transport assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignment removed successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> removeStudentFromRoute(
            @Parameter(description = "Assignment ID", required = true) @PathVariable String id) {
        transportService.removeStudentFromRoute(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== STATISTICS ENDPOINT ====================

    @Operation(summary = "Get transport statistics", description = "Retrieves overall transport system statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TransportStatsDTO.class)))
    })
    @GetMapping("/statistics")
    public ResponseEntity<TransportStatsDTO> getTransportStatistics() {
        TransportStatsDTO stats = transportService.getTransportStatistics();
        return ResponseEntity.ok(stats);
    }
}
