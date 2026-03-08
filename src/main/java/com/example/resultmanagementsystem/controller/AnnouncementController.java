package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.AnnouncementDTO;
import com.example.resultmanagementsystem.model.Announcement;
import com.example.resultmanagementsystem.services.CommunicationService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Announcements", description = "APIs for managing announcements including creating, updating, retrieving, and deactivating announcements for various target audiences.")
public class AnnouncementController {

    private final CommunicationService communicationService;

    @PostMapping
    @Operation(
            summary = "Create a new announcement",
            description = "Creates a new announcement with the specified details including type, priority, target audience, and content. The announcement is set as active by default."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Announcement created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Announcement.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Announcement> createAnnouncement(
            @Parameter(description = "Announcement details", required = true)
            @RequestBody AnnouncementDTO dto) {
        Announcement announcement = communicationService.createAnnouncement(dto);
        return new ResponseEntity<>(announcement, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get announcement by ID",
            description = "Retrieves a specific announcement by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Announcement.class))),
            @ApiResponse(responseCode = "404", description = "Announcement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Announcement> getAnnouncementById(
            @Parameter(description = "Unique identifier of the announcement", required = true)
            @PathVariable String id) {
        Announcement announcement = communicationService.getAnnouncementById(id);
        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get active announcements",
            description = "Retrieves a paginated list of all currently active announcements."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active announcements retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Announcement>> getActiveAnnouncements(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<Announcement> announcements = communicationService.getActiveAnnouncements(pageable);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    @Operation(
            summary = "Get announcements by type",
            description = "Retrieves a paginated list of announcements filtered by type (GENERAL, ACADEMIC, ADMINISTRATIVE, EMERGENCY, EVENT)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcements retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid announcement type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Announcement>> getAnnouncementsByType(
            @Parameter(description = "Announcement type", required = true)
            @PathVariable String type,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<Announcement> announcements = communicationService.getAnnouncementsByType(type, pageable);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @GetMapping("/audience/{audience}")
    @Operation(
            summary = "Get announcements by target audience",
            description = "Retrieves a paginated list of announcements filtered by target audience (ALL, STUDENTS, TEACHERS, PARENTS, STAFF, DEPARTMENT)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcements retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid audience type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Announcement>> getAnnouncementsByAudience(
            @Parameter(description = "Target audience type", required = true)
            @PathVariable String audience,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<Announcement> announcements = communicationService.getAnnouncementsByAudience(audience, pageable);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @GetMapping("/pinned")
    @Operation(
            summary = "Get pinned announcements",
            description = "Retrieves all active pinned announcements. Pinned announcements are displayed prominently."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pinned announcements retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Announcement.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Announcement>> getPinnedAnnouncements() {
        List<Announcement> announcements = communicationService.getPinnedAnnouncements();
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an announcement",
            description = "Updates an existing announcement with new details. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Announcement.class))),
            @ApiResponse(responseCode = "404", description = "Announcement not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Announcement> updateAnnouncement(
            @Parameter(description = "Unique identifier of the announcement", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated announcement details", required = true)
            @RequestBody AnnouncementDTO dto) {
        Announcement announcement = communicationService.updateAnnouncement(id, dto);
        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(
            summary = "Deactivate an announcement",
            description = "Deactivates an announcement by setting its active status to false. The announcement will no longer appear in active listings."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement deactivated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Announcement.class))),
            @ApiResponse(responseCode = "404", description = "Announcement not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Announcement> deactivateAnnouncement(
            @Parameter(description = "Unique identifier of the announcement", required = true)
            @PathVariable String id) {
        Announcement announcement = communicationService.deactivateAnnouncement(id);
        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
