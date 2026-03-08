package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.HostelAllocationDTO;
import com.example.resultmanagementsystem.Dto.HostelDTO;
import com.example.resultmanagementsystem.Dto.HostelRoomDTO;
import com.example.resultmanagementsystem.Dto.HostelStatsDTO;
import com.example.resultmanagementsystem.Dto.MaintenanceRequestDTO;
import com.example.resultmanagementsystem.model.Hostel;
import com.example.resultmanagementsystem.model.HostelAllocation;
import com.example.resultmanagementsystem.model.HostelMaintenanceRequest;
import com.example.resultmanagementsystem.model.HostelRoom;
import com.example.resultmanagementsystem.services.HostelService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hostel")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Hostel Management", description = "APIs for managing hostels, rooms, allocations, and maintenance requests")
public class HostelController {

    private final HostelService hostelService;

    // ==================== HOSTEL ENDPOINTS ====================

    @Operation(summary = "Create a new hostel", description = "Registers a new hostel in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hostel created successfully",
                    content = @Content(schema = @Schema(implementation = Hostel.class))),
            @ApiResponse(responseCode = "400", description = "Invalid hostel data")
    })
    @PostMapping("/admin/hostels")
    public ResponseEntity<Hostel> createHostel(@Valid @RequestBody HostelDTO hostelDTO) {
        log.info("Creating new hostel: {}", hostelDTO.getName());
        Hostel hostel = hostelService.createHostel(hostelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(hostel);
    }

    @Operation(summary = "Get hostel by ID", description = "Retrieves a specific hostel by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hostel found"),
            @ApiResponse(responseCode = "404", description = "Hostel not found")
    })
    @GetMapping("/hostels/{id}")
    public ResponseEntity<Hostel> getHostelById(
            @Parameter(description = "Hostel ID", required = true) @PathVariable String id) {
        Hostel hostel = hostelService.getHostelById(id);
        return ResponseEntity.ok(hostel);
    }

    @Operation(summary = "Get all hostels", description = "Retrieves a list of all hostels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hostels retrieved successfully")
    })
    @GetMapping("/hostels")
    public ResponseEntity<List<Hostel>> getAllHostels() {
        List<Hostel> hostels = hostelService.getAllHostels();
        return ResponseEntity.ok(hostels);
    }

    @Operation(summary = "Update a hostel", description = "Updates an existing hostel's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hostel updated successfully"),
            @ApiResponse(responseCode = "404", description = "Hostel not found")
    })
    @PutMapping("/admin/hostels/{id}")
    public ResponseEntity<Hostel> updateHostel(
            @Parameter(description = "Hostel ID", required = true) @PathVariable String id,
            @Valid @RequestBody HostelDTO hostelDTO) {
        Hostel hostel = hostelService.updateHostel(id, hostelDTO);
        return ResponseEntity.ok(hostel);
    }

    // ==================== ROOM ENDPOINTS ====================

    @Operation(summary = "Add a room to hostel", description = "Creates a new room in a hostel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created successfully",
                    content = @Content(schema = @Schema(implementation = HostelRoom.class))),
            @ApiResponse(responseCode = "400", description = "Invalid room data")
    })
    @PostMapping("/admin/rooms")
    public ResponseEntity<HostelRoom> addRoom(@Valid @RequestBody HostelRoomDTO roomDTO) {
        log.info("Adding room {} to hostel {}", roomDTO.getRoomNumber(), roomDTO.getHostelId());
        HostelRoom room = hostelService.addRoom(roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @Operation(summary = "Get rooms by hostel", description = "Retrieves all rooms for a specific hostel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully")
    })
    @GetMapping("/rooms/hostel/{hostelId}")
    public ResponseEntity<List<HostelRoom>> getRoomsByHostel(
            @Parameter(description = "Hostel ID", required = true) @PathVariable String hostelId) {
        List<HostelRoom> rooms = hostelService.getRoomsByHostel(hostelId);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get available rooms", description = "Retrieves all rooms with available beds across all hostels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available rooms retrieved successfully")
    })
    @GetMapping("/rooms/available")
    public ResponseEntity<List<HostelRoom>> getAvailableRooms() {
        List<HostelRoom> rooms = hostelService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Update a room", description = "Updates an existing room's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room updated successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @PutMapping("/admin/rooms/{id}")
    public ResponseEntity<HostelRoom> updateRoom(
            @Parameter(description = "Room ID", required = true) @PathVariable String id,
            @Valid @RequestBody HostelRoomDTO roomDTO) {
        HostelRoom room = hostelService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(room);
    }

    // ==================== ALLOCATION ENDPOINTS ====================

    @Operation(summary = "Allocate student to hostel", description = "Creates a new hostel allocation for a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student allocated successfully",
                    content = @Content(schema = @Schema(implementation = HostelAllocation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid allocation data or room full")
    })
    @PostMapping("/allocations")
    public ResponseEntity<HostelAllocation> allocateStudent(
            @Valid @RequestBody HostelAllocationDTO allocationDTO) {
        HostelAllocation allocation = hostelService.allocateStudent(allocationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(allocation);
    }

    @Operation(summary = "Get student allocation", description = "Retrieves hostel allocations for a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allocations retrieved successfully")
    })
    @GetMapping("/allocations/student/{studentId}")
    public ResponseEntity<List<HostelAllocation>> getStudentAllocation(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId) {
        List<HostelAllocation> allocations = hostelService.getStudentAllocation(studentId);
        return ResponseEntity.ok(allocations);
    }

    @Operation(summary = "Get allocations by hostel", description = "Retrieves all allocations for a specific hostel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allocations retrieved successfully")
    })
    @GetMapping("/allocations/hostel/{hostelId}")
    public ResponseEntity<List<HostelAllocation>> getAllocationsByHostel(
            @Parameter(description = "Hostel ID", required = true) @PathVariable String hostelId) {
        List<HostelAllocation> allocations = hostelService.getAllocationsByHostel(hostelId);
        return ResponseEntity.ok(allocations);
    }

    @Operation(summary = "Vacate student from hostel", description = "Marks a student's hostel allocation as vacated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student vacated successfully"),
            @ApiResponse(responseCode = "404", description = "Allocation not found")
    })
    @PatchMapping("/allocations/{id}/vacate")
    public ResponseEntity<HostelAllocation> vacateStudent(
            @Parameter(description = "Allocation ID", required = true) @PathVariable String id) {
        HostelAllocation allocation = hostelService.vacateStudent(id);
        return ResponseEntity.ok(allocation);
    }

    @Operation(summary = "Transfer student to another room", description = "Transfers a student from current room to a new room/hostel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student transferred successfully"),
            @ApiResponse(responseCode = "404", description = "Allocation not found"),
            @ApiResponse(responseCode = "400", description = "Target room full or invalid data")
    })
    @PatchMapping("/allocations/{id}/transfer")
    public ResponseEntity<HostelAllocation> transferStudent(
            @Parameter(description = "Allocation ID", required = true) @PathVariable String id,
            @Valid @RequestBody HostelAllocationDTO newAllocationDTO) {
        HostelAllocation allocation = hostelService.transferStudent(id, newAllocationDTO);
        return ResponseEntity.ok(allocation);
    }

    // ==================== MAINTENANCE ENDPOINTS ====================

    @Operation(summary = "Create maintenance request", description = "Submits a new maintenance request for a hostel room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Maintenance request created successfully",
                    content = @Content(schema = @Schema(implementation = HostelMaintenanceRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/maintenance")
    public ResponseEntity<HostelMaintenanceRequest> createMaintenanceRequest(
            @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        HostelMaintenanceRequest request = hostelService.createMaintenanceRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @Operation(summary = "Get maintenance requests by hostel", description = "Retrieves all maintenance requests for a specific hostel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance requests retrieved successfully")
    })
    @GetMapping("/maintenance/hostel/{hostelId}")
    public ResponseEntity<List<HostelMaintenanceRequest>> getMaintenanceByHostel(
            @Parameter(description = "Hostel ID", required = true) @PathVariable String hostelId) {
        List<HostelMaintenanceRequest> requests = hostelService.getMaintenanceByHostel(hostelId);
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Update maintenance request status", description = "Updates the status of a maintenance request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @PatchMapping("/maintenance/{id}/status")
    public ResponseEntity<HostelMaintenanceRequest> updateMaintenanceStatus(
            @Parameter(description = "Maintenance request ID", required = true) @PathVariable String id,
            @RequestBody Map<String, String> statusMap) {
        HostelMaintenanceRequest.MaintenanceStatus status =
                HostelMaintenanceRequest.MaintenanceStatus.valueOf(statusMap.get("status"));
        HostelMaintenanceRequest request = hostelService.updateMaintenanceStatus(id, status);
        return ResponseEntity.ok(request);
    }

    // ==================== STATISTICS ENDPOINT ====================

    @Operation(summary = "Get hostel statistics", description = "Retrieves overall hostel system statistics including occupancy rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = HostelStatsDTO.class)))
    })
    @GetMapping("/statistics")
    public ResponseEntity<HostelStatsDTO> getHostelStatistics() {
        HostelStatsDTO stats = hostelService.getHostelStatistics();
        return ResponseEntity.ok(stats);
    }
}
