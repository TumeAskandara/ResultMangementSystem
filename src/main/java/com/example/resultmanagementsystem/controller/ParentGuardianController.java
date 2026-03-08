package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ParentDashboardDTO;
import com.example.resultmanagementsystem.Dto.ParentGuardianDTO;
import com.example.resultmanagementsystem.model.ParentGuardian;
import com.example.resultmanagementsystem.services.ParentGuardianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Parent/Guardian Management", description = "APIs for managing parent and guardian records, linking students, and accessing parent dashboards")
public class ParentGuardianController {

    private final ParentGuardianService parentGuardianService;

    @PostMapping
    @Operation(
            summary = "Create a new parent/guardian",
            description = "Creates a new parent or guardian record in the system. Validates email uniqueness and required fields."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Parent/guardian created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation errors",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Parent with this email already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> createParent(
            @Parameter(description = "Parent/guardian details", required = true)
            @Valid @RequestBody ParentGuardianDTO parentGuardianDTO
    ) {
        ParentGuardian parent = parentGuardianService.createParentGuardian(parentGuardianDTO);
        return new ResponseEntity<>(parent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get parent/guardian by ID",
            description = "Retrieves a specific parent or guardian record using their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parent/guardian retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent/guardian not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> getParentById(
            @Parameter(description = "Parent/guardian ID", required = true) @PathVariable String id
    ) {
        ParentGuardian parent = parentGuardianService.getParentById(id);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Get parent/guardian by email",
            description = "Retrieves a parent or guardian record using their email address."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parent/guardian retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent/guardian not found with the specified email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> getParentByEmail(
            @Parameter(description = "Email address of the parent/guardian", required = true)
            @RequestParam String email
    ) {
        ParentGuardian parent = parentGuardianService.getParentByEmail(email);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get parent/guardian by user ID",
            description = "Retrieves a parent or guardian record using their linked user account ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parent/guardian retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent/guardian not found for the specified user ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> getParentByUserId(
            @Parameter(description = "User ID linked to the parent/guardian", required = true) @PathVariable String userId
    ) {
        ParentGuardian parent = parentGuardianService.getParentByUserId(userId);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}")
    @Operation(
            summary = "Get parents of a student",
            description = "Retrieves all parent or guardian records linked to a specific student."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parents/guardians retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<ParentGuardian>> getParentsByStudentId(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId
    ) {
        List<ParentGuardian> parents = parentGuardianService.getParentsByStudentId(studentId);
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update parent/guardian",
            description = "Updates an existing parent or guardian record with new information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parent/guardian updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent/guardian not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> updateParent(
            @Parameter(description = "Parent/guardian ID", required = true) @PathVariable String id,
            @Parameter(description = "Updated parent/guardian details", required = true)
            @Valid @RequestBody ParentGuardianDTO parentGuardianDTO
    ) {
        ParentGuardian parent = parentGuardianService.updateParent(id, parentGuardianDTO);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @PostMapping("/{parentId}/link/{studentId}")
    @Operation(
            summary = "Link student to parent",
            description = "Creates a link between a parent/guardian and a student, allowing the parent to view the student's information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student linked to parent successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent or student not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> linkStudentToParent(
            @Parameter(description = "Parent/guardian ID", required = true) @PathVariable String parentId,
            @Parameter(description = "Student ID to link", required = true) @PathVariable String studentId
    ) {
        ParentGuardian parent = parentGuardianService.linkStudentToParent(parentId, studentId);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @DeleteMapping("/{parentId}/unlink/{studentId}")
    @Operation(
            summary = "Unlink student from parent",
            description = "Removes the link between a parent/guardian and a student."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student unlinked from parent successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentGuardian.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent not found or student not linked",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentGuardian> unlinkStudentFromParent(
            @Parameter(description = "Parent/guardian ID", required = true) @PathVariable String parentId,
            @Parameter(description = "Student ID to unlink", required = true) @PathVariable String studentId
    ) {
        ParentGuardian parent = parentGuardianService.unlinkStudentFromParent(parentId, studentId);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @GetMapping("/{parentId}/dashboard")
    @Operation(
            summary = "Get parent dashboard",
            description = "Retrieves an aggregated dashboard view for a parent, including summaries for all linked children covering attendance, grades, fees, and events."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParentDashboardDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parent not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<ParentDashboardDTO> getParentDashboard(
            @Parameter(description = "Parent/guardian ID", required = true) @PathVariable String parentId
    ) {
        ParentDashboardDTO dashboard = parentGuardianService.getParentDashboard(parentId);
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all parents/guardians",
            description = "Retrieves a paginated list of all parent and guardian records in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parents/guardians retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Page<ParentGuardian>> getAllParents(Pageable pageable) {
        Page<ParentGuardian> parents = parentGuardianService.getAllParents(pageable);
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
