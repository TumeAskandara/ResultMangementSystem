package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.model.AdmissionApplication;
import com.example.resultmanagementsystem.services.AdmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/admissions")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Admission Management", description = "APIs for managing student admission applications")
public class AdmissionController {

    private final AdmissionService admissionService;

    @Operation(
            summary = "Submit admission application",
            description = "Submit a new admission application with applicant details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application submitted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO> submitApplication(
            @Valid @RequestBody AdmissionApplicationDTO applicationDTO) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            AdmissionApplication application = admissionService.applyForAdmission(applicationDTO);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusDescription("CREATED")
                            .message("Admission application submitted successfully")
                            .build())
                    .data(application)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error submitting admission application: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to submit admission application")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get application by ID",
            description = "Retrieve an admission application by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getApplicationById(
            @Parameter(description = "Unique identifier of the admission application", required = true)
            @PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            AdmissionApplication application = admissionService.getApplicationById(id);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Application retrieved successfully")
                            .build())
                    .data(application)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving application {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Application not found")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get applications by status",
            description = "Retrieve admission applications filtered by status with pagination support"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDTO> getApplicationsByStatus(
            @Parameter(description = "Application status (PENDING, UNDER_REVIEW, ACCEPTED, REJECTED, WAITLISTED, ENROLLED)", required = true)
            @PathVariable String status,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Page<AdmissionApplication> applications = admissionService.getApplicationsByStatus(
                    status, PageRequest.of(page, size));

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Applications retrieved successfully")
                            .build())
                    .data(applications.getContent())
                    .paginationDto(PaginationDTO.builder()
                            .count(applications.getNumberOfElements())
                            .total((int) applications.getTotalElements())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving applications by status {}: {}", status, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to retrieve applications by status")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get applications by department",
            description = "Retrieve all admission applications for a specific department"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No applications found for the department")
    })
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ResponseDTO> getApplicationsByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<AdmissionApplication> applications = admissionService.getApplicationsByDepartment(departmentId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Applications retrieved successfully")
                            .build())
                    .data(applications)
                    .paginationDto(PaginationDTO.builder()
                            .count(applications.size())
                            .total(applications.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving applications for department {}: {}", departmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Applications not found for department")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get applications by academic year",
            description = "Retrieve all admission applications for a specific academic year"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No applications found for the academic year")
    })
    @GetMapping("/academic-year/{year}")
    public ResponseEntity<ResponseDTO> getApplicationsByAcademicYear(
            @Parameter(description = "Academic year (e.g., 2024-2025)", required = true)
            @PathVariable String year) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<AdmissionApplication> applications = admissionService.getApplicationsByAcademicYear(year);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Applications retrieved successfully")
                            .build())
                    .data(applications)
                    .paginationDto(PaginationDTO.builder()
                            .count(applications.size())
                            .total(applications.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving applications for academic year {}: {}", year, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Applications not found for academic year")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get application by email",
            description = "Retrieve an admission application by the applicant's email address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Application not found for the given email")
    })
    @GetMapping("/email")
    public ResponseEntity<ResponseDTO> getApplicationByEmail(
            @Parameter(description = "Applicant's email address", required = true, example = "john@example.com")
            @RequestParam String email) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            AdmissionApplication application = admissionService.getApplicationByEmail(email);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Application retrieved successfully")
                            .build())
                    .data(application)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving application by email {}: {}", email, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Application not found for email")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Review admission application",
            description = "Review and update the status of an admission application with review notes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application reviewed successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PutMapping("/{id}/review")
    public ResponseEntity<ResponseDTO> reviewApplication(
            @Parameter(description = "Unique identifier of the admission application", required = true)
            @PathVariable String id,
            @RequestBody AdmissionReviewDTO reviewDTO) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            AdmissionApplication application = admissionService.reviewApplication(id, reviewDTO);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Application reviewed successfully")
                            .build())
                    .data(application)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error reviewing application {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to review application")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get admission statistics",
            description = "Retrieve admission application statistics (counts by status) for a specific academic year"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics/{academicYear}")
    public ResponseEntity<ResponseDTO> getAdmissionStatistics(
            @Parameter(description = "Academic year (e.g., 2024-2025)", required = true)
            @PathVariable String academicYear) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Map<String, Long> statistics = admissionService.getAdmissionStatistics(academicYear);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Admission statistics retrieved successfully")
                            .build())
                    .data(statistics)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving admission statistics for year {}: {}", academicYear, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("Failed to retrieve admission statistics")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
