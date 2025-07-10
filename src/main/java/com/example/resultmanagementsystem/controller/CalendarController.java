package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.MetaDTO;
import com.example.resultmanagementsystem.Dto.ResponseDTO;
import com.example.resultmanagementsystem.model.CalendarEvent;
import com.example.resultmanagementsystem.services.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Calendar Management", description = "APIs for managing school calendar events and notifications")
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(
            summary = "Create a new calendar event",
            description = "Creates a new calendar event for the school system. The event can be targeted to specific departments or all departments. Automatic notifications will be sent to relevant students."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Event created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid event data provided - includes JSON parsing errors, validation errors, or malformed request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @PostMapping("/create_events")
    public ResponseEntity<ResponseDTO> createEvent(
            @Parameter(description = "Calendar event details", required = true)
            @Valid @RequestBody CalendarEvent event
    ) {
        log.info("Creating calendar event: {}", event);

        CalendarEvent createdEvent = calendarService.createEvent(event);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Calendar event created successfully")
                        .build())
                .data(createdEvent)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an existing calendar event",
            description = "Updates an existing calendar event by its ID. Students will be notified about the changes made to the event."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Event updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data provided - includes JSON parsing errors, validation errors, or malformed request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @PutMapping("/events/{eventId}")
    public ResponseEntity<ResponseDTO> updateEvent(
            @Parameter(description = "Unique identifier of the event to update", required = true)
            @PathVariable @NotBlank String eventId,
            @Parameter(description = "Updated event details", required = true)
            @Valid @RequestBody CalendarEvent event
    ) {
        log.info("Updating calendar event with ID: {}", eventId);

        CalendarEvent updatedEvent = calendarService.updateEvent(eventId, event);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Calendar event updated successfully")
                        .build())
                .data(updatedEvent)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a calendar event",
            description = "Deletes a calendar event by its ID. Cancellation notifications will be sent to all affected students."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Event deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<ResponseDTO> deleteEvent(
            @Parameter(description = "Unique identifier of the event to delete", required = true)
            @PathVariable @NotBlank String eventId
    ) {
        log.info("Deleting calendar event with ID: {}", eventId);

        calendarService.deleteEvent(eventId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Calendar event deleted successfully")
                        .build())
                .data("Event with ID: " + eventId + " has been deleted")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get events by department",
            description = "Retrieves all calendar events for a specific department. This includes department-specific events and general events applicable to all departments."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Events retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @GetMapping("/events/department/{departmentId}")
    public ResponseEntity<ResponseDTO> getEventsByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable @NotBlank String departmentId
    ) {
        log.info("Retrieving events for department: {}", departmentId);

        List<CalendarEvent> events = calendarService.getEventsByDepartment(departmentId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Department events retrieved successfully")
                        .build())
                .data(events)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get events by date range",
            description = "Retrieves calendar events within a specified date range. Can be filtered by department or return events for all departments."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Events retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid date format or range - includes parsing errors for date parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @GetMapping("/events")
    public ResponseEntity<ResponseDTO> getEventsByDateRange(
            @Parameter(
                    description = "Start date and time for the range (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)",
                    required = true,
                    example = "2025-07-01T00:00:00"
            )
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(
                    description = "End date and time for the range (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)",
                    required = true,
                    example = "2025-07-31T23:59:59"
            )
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(
                    description = "Optional department ID to filter events. If not provided, returns events for all departments",
                    required = false
            )
            @RequestParam(required = false) String departmentId
    ) {
        log.info("Retrieving events for date range: {} to {}, department: {}", startDate, endDate, departmentId);

        // Validate date range
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        List<CalendarEvent> events = calendarService.getEventsByDateRange(startDate, endDate, departmentId);

        String message = departmentId != null ?
                "Events retrieved successfully for department: " + departmentId :
                "All events retrieved successfully for date range";

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message(message)
                        .build())
                .data(events)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get events for a specific student",
            description = "Retrieves all calendar events relevant to a specific student based on their department affiliation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student events retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @GetMapping("/events/student/{studentId}")
    public ResponseEntity<ResponseDTO> getStudentEvents(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable @NotBlank String studentId
    ) {
        log.info("Retrieving events for student: {}", studentId);

        List<CalendarEvent> events = calendarService.getStudentEvents(studentId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Student events retrieved successfully")
                        .build())
                .data(events)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Trigger manual event notifications",
            description = "Manually triggers the notification system to send pending notifications for upcoming events. This is primarily used for testing and administrative purposes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notifications processed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @PostMapping("/events/{eventId}/notify")
    public ResponseEntity<ResponseDTO> sendEventNotification(
            @Parameter(description = "Unique identifier of the event to send notifications for", required = true)
            @PathVariable @NotBlank String eventId
    ) {
        log.info("Processing notifications for event: {}", eventId);

        // Manual notification trigger for testing
        calendarService.processScheduledNotifications();

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Event notifications processed successfully")
                        .build())
                .data("Notifications have been sent for pending events")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}