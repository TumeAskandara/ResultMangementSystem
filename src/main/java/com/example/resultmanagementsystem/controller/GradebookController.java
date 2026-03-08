package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.GradeAppealDTO;
import com.example.resultmanagementsystem.Dto.GradeAppealReviewDTO;
import com.example.resultmanagementsystem.Dto.GradeCategoryDTO;
import com.example.resultmanagementsystem.Dto.GradeScaleDTO;
import com.example.resultmanagementsystem.model.GradeAppeal;
import com.example.resultmanagementsystem.model.GradeCategory;
import com.example.resultmanagementsystem.model.GradeScale;
import com.example.resultmanagementsystem.services.GradebookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/gradebook")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gradebook Management", description = "APIs for managing grade categories, grade scales, grade appeals, and weighted grade calculations")
public class GradebookController {

    private final GradebookService gradebookService;

    @Autowired
    public GradebookController(GradebookService gradebookService) {
        this.gradebookService = gradebookService;
    }

    // ========== Grade Category Endpoints ==========

    @PostMapping("/categories")
    @Operation(summary = "Create a grade category", description = "Creates a new grade category with a name, weight, and association to a course and department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grade category created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeCategory.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<GradeCategory> createCategory(
            @Parameter(description = "Grade category data", required = true)
            @RequestBody GradeCategoryDTO dto) {
        GradeCategory category = gradebookService.createGradeCategory(dto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping("/categories/course/{courseId}")
    @Operation(summary = "Get categories by course", description = "Retrieves all grade categories associated with a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeCategory.class))),
            @ApiResponse(responseCode = "404", description = "No categories found for the course",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<GradeCategory>> getCategoriesByCourse(
            @Parameter(description = "Course ID", required = true, example = "CSC101")
            @PathVariable String courseId) {
        return ResponseEntity.ok(gradebookService.getCategoriesByCourse(courseId));
    }

    @GetMapping("/categories/department/{departmentId}")
    @Operation(summary = "Get categories by department", description = "Retrieves all grade categories associated with a specific department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeCategory.class))),
            @ApiResponse(responseCode = "404", description = "No categories found for the department",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<GradeCategory>> getCategoriesByDepartment(
            @Parameter(description = "Department ID", required = true, example = "DEPT001")
            @PathVariable String departmentId) {
        return ResponseEntity.ok(gradebookService.getCategoriesByDepartment(departmentId));
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "Update a grade category", description = "Updates an existing grade category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeCategory.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<GradeCategory> updateCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable String id,
            @Parameter(description = "Updated category data", required = true) @RequestBody GradeCategoryDTO dto) {
        return ResponseEntity.ok(gradebookService.updateGradeCategory(id, dto));
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Delete a grade category", description = "Permanently deletes a grade category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> deleteCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable String id) {
        gradebookService.deleteGradeCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Grade category with ID " + id + " successfully deleted");
        return ResponseEntity.ok(response);
    }

    // ========== Grade Scale Endpoints ==========

    @PostMapping("/scales")
    @Operation(summary = "Create a grade scale", description = "Creates a new grade scale with entries defining grade boundaries, points, and descriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grade scale created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeScale.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<GradeScale> createScale(
            @Parameter(description = "Grade scale data", required = true) @RequestBody GradeScaleDTO dto) {
        GradeScale scale = gradebookService.createGradeScale(dto);
        return new ResponseEntity<>(scale, HttpStatus.CREATED);
    }

    @GetMapping("/scales/department/{departmentId}")
    @Operation(summary = "Get grade scales by department", description = "Retrieves all grade scales for a specific department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade scales retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeScale.class))),
            @ApiResponse(responseCode = "404", description = "No grade scales found for the department",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<GradeScale>> getScalesByDepartment(
            @Parameter(description = "Department ID", required = true, example = "DEPT001")
            @PathVariable String departmentId) {
        return ResponseEntity.ok(gradebookService.getGradeScaleByDepartment(departmentId));
    }

    @GetMapping("/scales/default")
    @Operation(summary = "Get default grade scale", description = "Retrieves the system default grade scale used when no department-specific scale is configured")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default grade scale retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeScale.class))),
            @ApiResponse(responseCode = "404", description = "No default grade scale found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<GradeScale> getDefaultScale() {
        return ResponseEntity.ok(gradebookService.getDefaultGradeScale());
    }

    // ========== Grade Appeal Endpoints ==========

    @PostMapping("/appeals")
    @Operation(summary = "Submit a grade appeal", description = "Allows a student to submit an appeal for a grade review on a specific result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appeal submitted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeAppeal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid appeal data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<GradeAppeal> submitAppeal(
            @Parameter(description = "Grade appeal data", required = true) @RequestBody GradeAppealDTO dto) {
        GradeAppeal appeal = gradebookService.submitGradeAppeal(dto);
        return new ResponseEntity<>(appeal, HttpStatus.CREATED);
    }

    @GetMapping("/appeals/student/{studentId}")
    @Operation(summary = "Get appeals by student", description = "Retrieves all grade appeals submitted by a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appeals retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeAppeal.class))),
            @ApiResponse(responseCode = "404", description = "No appeals found for the student",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<GradeAppeal>> getAppealsByStudent(
            @Parameter(description = "Student ID", required = true, example = "STU001")
            @PathVariable String studentId) {
        return ResponseEntity.ok(gradebookService.getAppealsByStudent(studentId));
    }

    @GetMapping("/appeals/course/{courseId}")
    @Operation(summary = "Get appeals by course", description = "Retrieves all grade appeals for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appeals retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeAppeal.class))),
            @ApiResponse(responseCode = "404", description = "No appeals found for the course",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<GradeAppeal>> getAppealsByCourse(
            @Parameter(description = "Course ID", required = true, example = "CSC101")
            @PathVariable String courseId) {
        return ResponseEntity.ok(gradebookService.getAppealsByCourse(courseId));
    }

    @GetMapping("/appeals/pending")
    @Operation(summary = "Get pending appeals", description = "Retrieves all pending grade appeals with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending appeals retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<GradeAppeal>> getPendingAppeals(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(gradebookService.getPendingAppeals(pageable));
    }

    @PutMapping("/appeals/{id}/review")
    @Operation(summary = "Review a grade appeal", description = "Allows a reviewer to approve or reject a grade appeal with review notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appeal reviewed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeAppeal.class))),
            @ApiResponse(responseCode = "404", description = "Appeal not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<GradeAppeal> reviewAppeal(
            @Parameter(description = "Appeal ID", required = true) @PathVariable String id,
            @Parameter(description = "Review data", required = true) @RequestBody GradeAppealReviewDTO dto) {
        return ResponseEntity.ok(gradebookService.reviewAppeal(id, dto));
    }

    // ========== Weighted Grade Endpoint ==========

    @GetMapping("/weighted-grade/student/{studentId}/course/{courseId}")
    @Operation(summary = "Calculate weighted grade", description = "Calculates the weighted grade for a student in a specific course based on grade categories and their weights")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Weighted grade calculated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Student or course not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> calculateWeightedGrade(
            @Parameter(description = "Student ID", required = true, example = "STU001") @PathVariable String studentId,
            @Parameter(description = "Course ID", required = true, example = "CSC101") @PathVariable String courseId) {
        return ResponseEntity.ok(gradebookService.calculateWeightedGrade(studentId, courseId));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
