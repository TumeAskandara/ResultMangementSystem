package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.StaffDTO;
import com.example.resultmanagementsystem.model.Staff;
import com.example.resultmanagementsystem.services.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Staff Management", description = "APIs for managing staff records including creation, retrieval, updates, and deletion of HR and staff information.")
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @Operation(
            summary = "Create a new staff member",
            description = "Creates a new staff record in the system with all necessary personal, professional, and financial details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Staff member created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Staff.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Staff with this email already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Staff> createStaff(
            @Parameter(description = "Staff details", required = true)
            @RequestBody StaffDTO staffDTO) {
        Staff staff = staffService.createStaff(staffDTO);
        return new ResponseEntity<>(staff, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get staff member by ID",
            description = "Retrieves a specific staff member's complete information using their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff member retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Staff.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Staff> getStaffById(
            @Parameter(description = "Unique identifier of the staff member", required = true)
            @PathVariable String id) {
        Staff staff = staffService.getStaffById(id);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Get staff member by email",
            description = "Retrieves a staff member's information using their email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff member retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Staff.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found with the specified email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Staff> getStaffByEmail(
            @Parameter(description = "Email address of the staff member", required = true, example = "john.doe@school.edu")
            @RequestParam String email) {
        Staff staff = staffService.getStaffByEmail(email);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all staff members (paginated)",
            description = "Retrieves a paginated list of all staff members registered in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff members retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<Staff>> getAllStaff(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Staff> staffPage = staffService.getAllStaff(pageable);
        return new ResponseEntity<>(staffPage, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get staff members by department",
            description = "Retrieves all staff members belonging to a specific department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff members retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<Staff>> getStaffByDepartment(
            @Parameter(description = "Department ID", required = true)
            @PathVariable String departmentId) {
        List<Staff> staffList = staffService.getStaffByDepartment(departmentId);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/type/{staffType}")
    @Operation(
            summary = "Get staff members by type",
            description = "Retrieves all staff members of a specific type (TEACHING, NON_TEACHING, ADMINISTRATIVE)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff members retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Invalid staff type",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<Staff>> getStaffByType(
            @Parameter(description = "Staff type", required = true, example = "TEACHING")
            @PathVariable Staff.StaffType staffType) {
        List<Staff> staffList = staffService.getStaffByType(staffType);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a staff member",
            description = "Updates an existing staff member's information. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff member updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Staff.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Staff> updateStaff(
            @Parameter(description = "Staff member ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated staff details", required = true)
            @RequestBody StaffDTO staffDTO) {
        Staff updatedStaff = staffService.updateStaff(id, staffDTO);
        return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update staff employment status",
            description = "Updates the employment status of a staff member (ACTIVE, ON_LEAVE, SUSPENDED, TERMINATED, RETIRED)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Staff.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Staff> updateEmploymentStatus(
            @Parameter(description = "Staff member ID", required = true)
            @PathVariable String id,
            @Parameter(description = "New employment status", required = true, example = "ACTIVE")
            @RequestParam Staff.EmploymentStatus status) {
        Staff updatedStaff = staffService.updateEmploymentStatus(id, status);
        return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a staff member",
            description = "Permanently removes a staff member record from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff member deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> deleteStaff(
            @Parameter(description = "Staff member ID", required = true)
            @PathVariable String id) {
        staffService.deleteStaff(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Staff member successfully deleted.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
