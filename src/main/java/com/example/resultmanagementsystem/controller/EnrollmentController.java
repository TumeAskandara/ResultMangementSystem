package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.model.Enrollment;
import com.example.resultmanagementsystem.services.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Enrollment Management", description = "APIs for managing student enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(
            summary = "Enroll student",
            description = "Create a new enrollment record for a student"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student enrolled successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid enrollment data")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO> enrollStudent(@RequestBody EnrollmentDTO enrollmentDTO) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Enrollment enrollment = enrollmentService.enrollStudent(enrollmentDTO);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusDescription("CREATED")
                            .message("Student enrolled successfully")
                            .build())
                    .data(enrollment)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error enrolling student: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to enroll student")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get enrollment by ID",
            description = "Retrieve an enrollment record by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getEnrollmentById(
            @Parameter(description = "Unique identifier of the enrollment", required = true)
            @PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Enrollment retrieved successfully")
                            .build())
                    .data(enrollment)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving enrollment {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Enrollment not found")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get enrollments by student",
            description = "Retrieve all enrollment records for a specific student"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No enrollments found for the student")
    })
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponseDTO> getEnrollmentsByStudent(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Enrollments retrieved successfully")
                            .build())
                    .data(enrollments)
                    .paginationDto(PaginationDTO.builder()
                            .count(enrollments.size())
                            .total(enrollments.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving enrollments for student {}: {}", studentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Enrollments not found for student")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get enrollments by department",
            description = "Retrieve enrollment records for a specific department with pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No enrollments found for the department")
    })
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ResponseDTO> getEnrollmentsByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Page<Enrollment> enrollments = enrollmentService.getEnrollmentsByDepartment(
                    departmentId, PageRequest.of(page, size));

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Enrollments retrieved successfully")
                            .build())
                    .data(enrollments.getContent())
                    .paginationDto(PaginationDTO.builder()
                            .count(enrollments.getNumberOfElements())
                            .total((int) enrollments.getTotalElements())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving enrollments for department {}: {}", departmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Enrollments not found for department")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get enrollments by class",
            description = "Retrieve all enrollment records for a specific class"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No enrollments found for the class")
    })
    @GetMapping("/class/{classId}")
    public ResponseEntity<ResponseDTO> getEnrollmentsByClass(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByClass(classId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Enrollments retrieved successfully")
                            .build())
                    .data(enrollments)
                    .paginationDto(PaginationDTO.builder()
                            .count(enrollments.size())
                            .total(enrollments.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving enrollments for class {}: {}", classId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Enrollments not found for class")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Update enrollment status",
            description = "Update the status of an existing enrollment record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment status updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateEnrollmentStatus(
            @Parameter(description = "Unique identifier of the enrollment", required = true)
            @PathVariable String id,
            @Parameter(description = "New enrollment status (ACTIVE, SUSPENDED, WITHDRAWN, GRADUATED, TRANSFERRED)", required = true)
            @RequestParam String status) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Enrollment enrollment = enrollmentService.updateEnrollmentStatus(id, status);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Enrollment status updated successfully")
                            .build())
                    .data(enrollment)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating enrollment status {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to update enrollment status")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get enrollment statistics",
            description = "Retrieve enrollment statistics (counts by status) for a specific academic year"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics/{academicYear}")
    public ResponseEntity<ResponseDTO> getEnrollmentStatistics(
            @Parameter(description = "Academic year (e.g., 2024-2025)", required = true)
            @PathVariable String academicYear) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Map<String, Long> statistics = enrollmentService.getEnrollmentStatistics(academicYear);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Enrollment statistics retrieved successfully")
                            .build())
                    .data(statistics)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving enrollment statistics for year {}: {}", academicYear, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("Failed to retrieve enrollment statistics")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get active enrollments by academic year",
            description = "Retrieve all active enrollment records for a specific academic year with pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No active enrollments found for the academic year")
    })
    @GetMapping("/active/{academicYear}")
    public ResponseEntity<ResponseDTO> getActiveEnrollmentsByAcademicYear(
            @Parameter(description = "Academic year (e.g., 2024-2025)", required = true)
            @PathVariable String academicYear,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Page<Enrollment> enrollments = enrollmentService.getActiveEnrollmentsByAcademicYear(
                    academicYear, PageRequest.of(page, size));

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Active enrollments retrieved successfully")
                            .build())
                    .data(enrollments.getContent())
                    .paginationDto(PaginationDTO.builder()
                            .count(enrollments.getNumberOfElements())
                            .total((int) enrollments.getTotalElements())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving active enrollments for year {}: {}", academicYear, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Active enrollments not found for academic year")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
