package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.AuditLogDTO;
import com.example.resultmanagementsystem.Dto.AuditSearchDTO;
import com.example.resultmanagementsystem.services.AuditService;
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
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Audit Logging", description = "APIs for viewing audit logs and tracking system activity. Admin-only access for monitoring user actions, data changes, and system events.")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(
            summary = "Get audit logs by entity",
            description = "Retrieves all audit log entries for a specific entity type and entity ID. Useful for tracking the history of changes to a particular record."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditLogDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntity(
            @Parameter(description = "Type of entity (e.g., Student, Result, Course)", required = true)
            @PathVariable String entityType,
            @Parameter(description = "Unique identifier of the entity", required = true)
            @PathVariable String entityId) {
        List<AuditLogDTO> logs = auditService.getAuditLogsByEntity(entityType, entityId);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/user/{performedBy}")
    @Operation(
            summary = "Get audit logs by user",
            description = "Retrieves paginated audit log entries for actions performed by a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByUser(
            @Parameter(description = "Identifier of the user who performed the actions", required = true)
            @PathVariable String performedBy,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLogDTO> logs = auditService.getAuditLogsByUser(performedBy, pageable);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/action/{action}")
    @Operation(
            summary = "Get audit logs by action type",
            description = "Retrieves paginated audit log entries filtered by action type (e.g., CREATE, UPDATE, DELETE, LOGIN)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid action type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByAction(
            @Parameter(description = "Action type (CREATE, READ, UPDATE, DELETE, LOGIN, LOGOUT, EXPORT, IMPORT, STATUS_CHANGE)", required = true)
            @PathVariable String action,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLogDTO> logs = auditService.getAuditLogsByAction(action, pageable);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get audit logs by date range",
            description = "Retrieves paginated audit log entries within a specified date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByDateRange(
            @Parameter(description = "Start date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLogDTO> logs = auditService.getAuditLogsByDateRange(startDate, endDate, pageable);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @PostMapping("/search")
    @Operation(
            summary = "Search audit logs",
            description = "Searches audit logs using multiple criteria including entity type, entity ID, performer, action type, and date range. Returns paginated results."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuditLogDTO>> searchAuditLogs(
            @Parameter(description = "Search criteria", required = true)
            @RequestBody AuditSearchDTO searchDTO,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLogDTO> logs = auditService.searchAuditLogs(searchDTO, pageable);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/recent")
    @Operation(
            summary = "Get recent activity",
            description = "Retrieves the most recent audit log entries. Useful for displaying recent system activity on a dashboard."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recent activity retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditLogDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AuditLogDTO>> getRecentActivity(
            @Parameter(description = "Number of recent entries to retrieve")
            @RequestParam(defaultValue = "20") int limit) {
        List<AuditLogDTO> logs = auditService.getRecentActivity(limit);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
