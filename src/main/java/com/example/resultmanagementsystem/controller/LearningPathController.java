package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.LearningPathDTO;
import com.example.resultmanagementsystem.Dto.LearningPathProgressDTO;
import com.example.resultmanagementsystem.model.LearningPath;
import com.example.resultmanagementsystem.model.LearningPathProgress;
import com.example.resultmanagementsystem.services.LearningPathService;
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
@RequestMapping("/api/learning-paths")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Learning Paths", description = "APIs for managing learning paths including creation, enrollment, progress tracking, and publishing. Learning paths define ordered sequences of courses for students to follow.")
public class LearningPathController {

    private final LearningPathService learningPathService;

    @PostMapping
    @Operation(
            summary = "Create a new learning path",
            description = "Creates a new learning path with specified courses, prerequisites, difficulty level, and tags. The learning path is created in unpublished state."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Learning path created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPath.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPath> createLearningPath(
            @Parameter(description = "Learning path details", required = true)
            @RequestBody LearningPathDTO dto) {
        LearningPath learningPath = learningPathService.createLearningPath(dto);
        return new ResponseEntity<>(learningPath, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get learning path by ID",
            description = "Retrieves a specific learning path by its unique identifier including all details, enrolled students, and completed students."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Learning path retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPath.class))),
            @ApiResponse(responseCode = "404", description = "Learning path not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPath> getLearningPathById(
            @Parameter(description = "Unique identifier of the learning path", required = true)
            @PathVariable String id) {
        LearningPath learningPath = learningPathService.getLearningPathById(id);
        return new ResponseEntity<>(learningPath, HttpStatus.OK);
    }

    @GetMapping("/published")
    @Operation(
            summary = "Get published learning paths",
            description = "Retrieves a paginated list of all published learning paths available for enrollment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Published learning paths retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<LearningPath>> getPublishedLearningPaths(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LearningPath> paths = learningPathService.getPublishedLearningPaths(pageable);
        return new ResponseEntity<>(paths, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get learning paths by department",
            description = "Retrieves all learning paths associated with a specific department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Learning paths retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LearningPath>> getLearningPathsByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId) {
        List<LearningPath> paths = learningPathService.getLearningPathsByDepartment(departmentId);
        return new ResponseEntity<>(paths, HttpStatus.OK);
    }

    @PostMapping("/{pathId}/enroll/{studentId}")
    @Operation(
            summary = "Enroll student in learning path",
            description = "Enrolls a student in a learning path and creates a progress tracking record. Fails if the student is already enrolled."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student enrolled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPathProgress.class))),
            @ApiResponse(responseCode = "400", description = "Student already enrolled"),
            @ApiResponse(responseCode = "404", description = "Learning path not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPathProgress> enrollStudent(
            @Parameter(description = "Unique identifier of the learning path", required = true)
            @PathVariable String pathId,
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        LearningPathProgress progress = learningPathService.enrollStudent(pathId, studentId);
        return new ResponseEntity<>(progress, HttpStatus.CREATED);
    }

    @PutMapping("/{pathId}/progress/{studentId}")
    @Operation(
            summary = "Update student progress",
            description = "Updates a student's progress on a learning path by marking a course as completed. Automatically calculates the progress percentage and checks for path completion."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPathProgress.class))),
            @ApiResponse(responseCode = "404", description = "Progress record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPathProgress> updateProgress(
            @Parameter(description = "Unique identifier of the learning path", required = true)
            @PathVariable String pathId,
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId,
            @Parameter(description = "ID of the completed course", required = true)
            @RequestParam String completedCourseId) {
        LearningPathProgress progress = learningPathService.updateProgress(pathId, studentId, completedCourseId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @GetMapping("/progress/{studentId}/{pathId}")
    @Operation(
            summary = "Get student progress for a learning path",
            description = "Retrieves the progress details of a specific student on a specific learning path."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPathProgressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Progress record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPathProgressDTO> getStudentProgress(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId,
            @Parameter(description = "Unique identifier of the learning path", required = true)
            @PathVariable String pathId) {
        LearningPathProgressDTO progress = learningPathService.getStudentProgress(studentId, pathId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @GetMapping("/progress/student/{studentId}")
    @Operation(
            summary = "Get all learning path progress for a student",
            description = "Retrieves progress details for all learning paths a student is enrolled in."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress records retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LearningPathProgressDTO>> getStudentAllPaths(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        List<LearningPathProgressDTO> progress = learningPathService.getStudentAllPaths(studentId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @PatchMapping("/{id}/publish")
    @Operation(
            summary = "Publish a learning path",
            description = "Publishes a learning path, making it available for student enrollment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Learning path published successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPath.class))),
            @ApiResponse(responseCode = "404", description = "Learning path not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPath> publishLearningPath(
            @Parameter(description = "Unique identifier of the learning path", required = true)
            @PathVariable String id) {
        LearningPath learningPath = learningPathService.publishLearningPath(id);
        return new ResponseEntity<>(learningPath, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a learning path",
            description = "Updates an existing learning path with new details. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Learning path updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningPath.class))),
            @ApiResponse(responseCode = "404", description = "Learning path not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LearningPath> updateLearningPath(
            @Parameter(description = "Unique identifier of the learning path", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated learning path details", required = true)
            @RequestBody LearningPathDTO dto) {
        LearningPath learningPath = learningPathService.updateLearningPath(id, dto);
        return new ResponseEntity<>(learningPath, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
