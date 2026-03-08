package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.CourseModuleDTO;
import com.example.resultmanagementsystem.Dto.LearningProgressDTO;
import com.example.resultmanagementsystem.Dto.LessonDTO;
import com.example.resultmanagementsystem.model.CourseModule;
import com.example.resultmanagementsystem.model.LearningProgress;
import com.example.resultmanagementsystem.model.Lesson;
import com.example.resultmanagementsystem.services.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Learning Content Management", description = "APIs for managing course modules, lessons, and tracking student learning progress")
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    // ========== Module Endpoints ==========

    @PostMapping("/modules")
    @Operation(summary = "Create a course module", description = "Creates a new course module with title, description, order, and optional prerequisite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Module created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseModule.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<CourseModule> createModule(
            @Parameter(description = "Course module data", required = true) @RequestBody CourseModuleDTO dto) {
        CourseModule module = contentService.createModule(dto);
        return new ResponseEntity<>(module, HttpStatus.CREATED);
    }

    @GetMapping("/modules/course/{courseId}")
    @Operation(summary = "Get modules by course", description = "Retrieves all modules for a specific course ordered by their index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modules retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseModule.class))),
            @ApiResponse(responseCode = "404", description = "No modules found for the course",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<CourseModule>> getModulesByCourse(
            @Parameter(description = "Course ID", required = true, example = "CSC101") @PathVariable String courseId) {
        return ResponseEntity.ok(contentService.getModulesByCourse(courseId));
    }

    @PutMapping("/modules/{id}")
    @Operation(summary = "Update a course module", description = "Updates an existing course module by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseModule.class))),
            @ApiResponse(responseCode = "404", description = "Module not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<CourseModule> updateModule(
            @Parameter(description = "Module ID", required = true) @PathVariable String id,
            @Parameter(description = "Updated module data", required = true) @RequestBody CourseModuleDTO dto) {
        return ResponseEntity.ok(contentService.updateModule(id, dto));
    }

    @DeleteMapping("/modules/{id}")
    @Operation(summary = "Delete a course module", description = "Permanently deletes a course module by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Module not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> deleteModule(
            @Parameter(description = "Module ID", required = true) @PathVariable String id) {
        contentService.deleteModule(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Module with ID " + id + " successfully deleted");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/modules/reorder/{courseId}")
    @Operation(summary = "Reorder course modules", description = "Reorders modules for a course by providing an ordered list of module IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modules reordered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid module IDs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> reorderModules(
            @Parameter(description = "Course ID", required = true) @PathVariable String courseId,
            @Parameter(description = "Ordered list of module IDs", required = true) @RequestBody List<String> moduleIds) {
        contentService.reorderModules(courseId, moduleIds);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Modules reordered successfully for course " + courseId);
        return ResponseEntity.ok(response);
    }

    // ========== Lesson Endpoints ==========

    @PostMapping("/lessons")
    @Operation(summary = "Create a lesson", description = "Creates a new lesson within a module with content, type, and optional attachments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Lesson> createLesson(
            @Parameter(description = "Lesson data", required = true) @RequestBody LessonDTO dto) {
        Lesson lesson = contentService.createLesson(dto);
        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    @GetMapping("/lessons/module/{moduleId}")
    @Operation(summary = "Get lessons by module", description = "Retrieves all lessons within a specific module ordered by their index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the module",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<Lesson>> getLessonsByModule(
            @Parameter(description = "Module ID", required = true) @PathVariable String moduleId) {
        return ResponseEntity.ok(contentService.getLessonsByModule(moduleId));
    }

    @GetMapping("/lessons/course/{courseId}")
    @Operation(summary = "Get lessons by course", description = "Retrieves all lessons for a specific course ordered by their index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the course",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<Lesson>> getLessonsByCourse(
            @Parameter(description = "Course ID", required = true, example = "CSC101") @PathVariable String courseId) {
        return ResponseEntity.ok(contentService.getLessonsByCourse(courseId));
    }

    @PutMapping("/lessons/{id}")
    @Operation(summary = "Update a lesson", description = "Updates an existing lesson by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Lesson> updateLesson(
            @Parameter(description = "Lesson ID", required = true) @PathVariable String id,
            @Parameter(description = "Updated lesson data", required = true) @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(contentService.updateLesson(id, dto));
    }

    @DeleteMapping("/lessons/{id}")
    @Operation(summary = "Delete a lesson", description = "Permanently deletes a lesson by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> deleteLesson(
            @Parameter(description = "Lesson ID", required = true) @PathVariable String id) {
        contentService.deleteLesson(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lesson with ID " + id + " successfully deleted");
        return ResponseEntity.ok(response);
    }

    // ========== Progress Endpoints ==========

    @PostMapping("/progress")
    @Operation(summary = "Track learning progress", description = "Creates or updates learning progress for a student on a specific lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress tracked successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningProgress.class))),
            @ApiResponse(responseCode = "400", description = "Invalid progress data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LearningProgress> trackProgress(
            @Parameter(description = "Student ID", required = true) @RequestParam String studentId,
            @Parameter(description = "Lesson ID", required = true) @RequestParam String lessonId,
            @Parameter(description = "Progress data", required = true) @RequestBody LearningProgressDTO dto) {
        return ResponseEntity.ok(contentService.trackProgress(studentId, lessonId, dto));
    }

    @GetMapping("/progress/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get student progress", description = "Retrieves all learning progress records for a student in a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearningProgress.class))),
            @ApiResponse(responseCode = "404", description = "No progress found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<LearningProgress>> getStudentProgress(
            @Parameter(description = "Student ID", required = true, example = "STU001") @PathVariable String studentId,
            @Parameter(description = "Course ID", required = true, example = "CSC101") @PathVariable String courseId) {
        return ResponseEntity.ok(contentService.getStudentProgress(studentId, courseId));
    }

    @GetMapping("/progress/completion/{studentId}/{courseId}")
    @Operation(summary = "Get course completion percentage", description = "Calculates the completion percentage for a student in a specific course based on completed lessons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Completion percentage calculated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Student or course not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> getCourseCompletionPercentage(
            @Parameter(description = "Student ID", required = true, example = "STU001") @PathVariable String studentId,
            @Parameter(description = "Course ID", required = true, example = "CSC101") @PathVariable String courseId) {
        return ResponseEntity.ok(contentService.getCourseCompletionPercentage(studentId, courseId));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
