package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ResultDTO;
import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.services.ResultService;
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
@RequestMapping("/api/results")
@CrossOrigin(origins ="*", maxAge = 3600)
@Tag(name = "Result Management", description = "APIs for managing student academic results including CRUD operations, GPA calculations, and result retrieval by various criteria")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/student/email")
    @Operation(
            summary = "Get student results by email and semester and year",
            description = "Retrieves all academic results for a specific student based on their email address, semester, and academic year. Returns comprehensive result data including grades, credits, and calculated totals."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters - missing or malformed email, semester, or year",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No results found for the specified student, semester, and year",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving results",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, Object>> getResultsByStudentEmail(
            @Parameter(description = "Student's email address", required = true, example = "john.doe@university.edu")
            @RequestParam String email,
            @Parameter(description = "Academic semester (e.g., FIRST, SECOND)", required = true, example = "FIRST")
            @RequestParam String semester,
            @Parameter(description = "Academic year", required = true, example = "2024")
            @RequestParam String year
    ) {
        Map<String, Object> results = resultService.getResultsByStudentEmailAndSemester(email, semester, year);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    @Operation(
            summary = "Create a new academic result",
            description = "Creates a new academic result record for a student in a specific course. Validates input data including CA scores, exam scores, and credit units before creation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Result created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data - validation errors in result data, invalid JSON format, or missing required fields",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - Result already exists for this student and course combination",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while creating the result",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Result> createResult(
            @Parameter(description = "Result data transfer object containing all necessary result information", required = true)
            @RequestBody ResultDTO resultDTO
    ) {
        try {
            String studentId = resultDTO.getStudentId();
            String courseId = resultDTO.getCourseId();
            double ca = resultDTO.getCa();
            double exams = resultDTO.getExams();
            double credits = resultDTO.getCredit();

            Result createdResult = resultService.createResult(resultDTO);
            return new ResponseEntity<>(createdResult, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create result: " + e.getMessage());
        }
    }

    @GetMapping("/getResultById{id}")
    @Operation(
            summary = "Get result by ID",
            description = "Retrieves a specific academic result record using its unique identifier. Returns complete result information including student details, course information, and grades."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Result retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid result ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Result not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving the result",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Result> getResultById(
            @Parameter(description = "Unique identifier of the result record", required = true, example = "RES001")
            @PathVariable String id
    ) {
        try {
            Result result = resultService.getResultById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/student/getResultByStudentId{studentId}")
    @Operation(
            summary = "Get all results for a specific student",
            description = "Retrieves all academic results for a student across all courses and semesters. Returns comprehensive academic history including calculated metrics and performance indicators."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid student ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No results found for the specified student ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving student results",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, Object>> getResultsByStudentId(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId
    ) {
        Map<String,Object> results = resultService.getResultsByStudentId(studentId);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/course/getResultByCourseId{courseId}")
    @Operation(
            summary = "Get all results for a specific course",
            description = "Retrieves all academic results for a particular course across all students. Useful for course performance analysis and grade distribution reviews."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid course ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No results found for the specified course ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving course results",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Result>> getResultsByCourseId(
            @Parameter(description = "Unique identifier of the course", required = true, example = "CSC101")
            @PathVariable String courseId
    ) {
        List<Result> results = resultService.getResultsByCourseId(courseId);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}/gpa")
    @Operation(
            summary = "Calculate student's cumulative GPA",
            description = "Calculates and returns the cumulative Grade Point Average (GPA) for a student. Can be filtered by semester and year for specific period calculations."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "GPA calculated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid student ID, semester, or year format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found or no results available for GPA calculation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred during GPA calculation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, Object>> calculateCumulativeGPA(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId,
            @Parameter(description = "Specific semester for GPA calculation (optional)", required = false, example = "FIRST")
            @RequestParam(required = false) String semester,
            @Parameter(description = "Academic year for GPA calculation", required = true, example = "2024")
            @RequestParam String year
    ) {
        double cumulativeGPA = resultService.calculateGPABySemester(studentId,semester, year);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("cumulativeGPA", cumulativeGPA);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing result",
            description = "Updates CA and exam scores for an existing result record. Recalculates total scores and grades automatically based on the new input values."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Result updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data - malformed JSON, invalid score values, or missing required fields",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Result not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while updating the result",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Result> updateResult(
            @Parameter(description = "Unique identifier of the result to update", required = true, example = "RES001")
            @PathVariable String id,
            @Parameter(description = "Map containing 'ca' and 'exams' score values to update", required = true)
            @RequestBody Map<String, Object> request
    ) {
        try {
            double ca = Double.parseDouble(request.get("ca").toString());
            double exams = Double.parseDouble(request.get("exams").toString());

            Result updatedResult = resultService.updateResult(id, ca, exams);
            return new ResponseEntity<>(updatedResult, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a result record",
            description = "Permanently removes a result record from the system. This action cannot be undone and will affect GPA calculations and academic records."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Result deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid result ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Result not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cannot delete result due to data dependencies",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while deleting the result",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, String>> deleteResult(
            @Parameter(description = "Unique identifier of the result to delete", required = true, example = "RES001")
            @PathVariable String id
    ) {
        try {
            resultService.deleteResult(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Result with ID " + id + " successfully deleted");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Exception handler for IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation Error");
        errorResponse.put("message", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}