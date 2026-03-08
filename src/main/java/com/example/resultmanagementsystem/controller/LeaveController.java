package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.LeaveBalanceDTO;
import com.example.resultmanagementsystem.Dto.LeaveRequestDTO;
import com.example.resultmanagementsystem.Dto.LeaveReviewDTO;
import com.example.resultmanagementsystem.model.LeaveBalance;
import com.example.resultmanagementsystem.model.LeaveRequest;
import com.example.resultmanagementsystem.services.LeaveService;
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
@RequestMapping("/api/leave")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Leave Management", description = "APIs for managing leave requests, approvals, and leave balance tracking for staff members.")
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    @Operation(
            summary = "Apply for leave",
            description = "Submits a new leave request for a staff member. The request will be in PENDING status until reviewed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave request submitted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LeaveRequest> applyLeave(
            @Parameter(description = "Leave request details", required = true)
            @RequestBody LeaveRequestDTO leaveRequestDTO,
            @Parameter(description = "Staff ID of the applicant", required = true)
            @RequestParam String staffId) {
        LeaveRequest leaveRequest = leaveService.applyLeave(leaveRequestDTO, staffId);
        return new ResponseEntity<>(leaveRequest, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get leave request by ID",
            description = "Retrieves a specific leave request using its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveRequest.class))),
            @ApiResponse(responseCode = "404", description = "Leave request not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LeaveRequest> getLeaveById(
            @Parameter(description = "Leave request ID", required = true)
            @PathVariable String id) {
        LeaveRequest leaveRequest = leaveService.getLeaveById(id);
        return new ResponseEntity<>(leaveRequest, HttpStatus.OK);
    }

    @GetMapping("/staff/{staffId}")
    @Operation(
            summary = "Get leave requests by staff member",
            description = "Retrieves all leave requests submitted by a specific staff member."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave requests retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<LeaveRequest>> getLeavesByStaff(
            @Parameter(description = "Staff member ID", required = true)
            @PathVariable String staffId) {
        List<LeaveRequest> leaves = leaveService.getLeavesByStaff(staffId);
        return new ResponseEntity<>(leaves, HttpStatus.OK);
    }

    @GetMapping("/pending")
    @Operation(
            summary = "Get pending leave requests (paginated)",
            description = "Retrieves a paginated list of all pending leave requests awaiting review."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending leave requests retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<LeaveRequest>> getPendingLeaves(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> pendingLeaves = leaveService.getPendingLeaves(pageable);
        return new ResponseEntity<>(pendingLeaves, HttpStatus.OK);
    }

    @PutMapping("/{id}/review")
    @Operation(
            summary = "Review a leave request",
            description = "Approves or rejects a pending leave request. Only PENDING requests can be reviewed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request reviewed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveRequest.class))),
            @ApiResponse(responseCode = "400", description = "Leave request is not in PENDING status",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Leave request not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LeaveRequest> reviewLeave(
            @Parameter(description = "Leave request ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Review details", required = true)
            @RequestBody LeaveReviewDTO leaveReviewDTO) {
        LeaveRequest reviewedLeave = leaveService.reviewLeave(id, leaveReviewDTO);
        return new ResponseEntity<>(reviewedLeave, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel a leave request",
            description = "Cancels a pending leave request. Only the applicant can cancel their own pending leave requests."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request cancelled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveRequest.class))),
            @ApiResponse(responseCode = "400", description = "Leave request cannot be cancelled",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Leave request not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LeaveRequest> cancelLeave(
            @Parameter(description = "Leave request ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Staff ID of the applicant", required = true)
            @RequestParam String staffId) {
        LeaveRequest cancelledLeave = leaveService.cancelLeave(id, staffId);
        return new ResponseEntity<>(cancelledLeave, HttpStatus.OK);
    }

    @GetMapping("/balance/{staffId}")
    @Operation(
            summary = "Get leave balance for a staff member",
            description = "Retrieves the leave balance of a staff member for a specific academic year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave balance retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveBalance.class))),
            @ApiResponse(responseCode = "404", description = "Leave balance not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LeaveBalance> getLeaveBalance(
            @Parameter(description = "Staff member ID", required = true)
            @PathVariable String staffId,
            @Parameter(description = "Academic year", required = true, example = "2024-2025")
            @RequestParam String academicYear) {
        LeaveBalance leaveBalance = leaveService.getLeaveBalance(staffId, academicYear);
        return new ResponseEntity<>(leaveBalance, HttpStatus.OK);
    }

    @PostMapping("/balance/initialize")
    @Operation(
            summary = "Initialize leave balance",
            description = "Initializes default leave balance for a staff member for a specific academic year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave balance initialized successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveBalance.class))),
            @ApiResponse(responseCode = "400", description = "Leave balance already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LeaveBalance> initializeLeaveBalance(
            @Parameter(description = "Staff ID", required = true)
            @RequestParam String staffId,
            @Parameter(description = "Academic year", required = true, example = "2024-2025")
            @RequestParam String academicYear) {
        LeaveBalance leaveBalance = leaveService.initializeLeaveBalance(staffId, academicYear);
        return new ResponseEntity<>(leaveBalance, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
