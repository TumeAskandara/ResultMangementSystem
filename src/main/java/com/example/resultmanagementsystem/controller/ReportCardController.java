package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ReportCardRequestDTO;
import com.example.resultmanagementsystem.Dto.TranscriptRequestDTO;
import com.example.resultmanagementsystem.model.ReportCard;
import com.example.resultmanagementsystem.model.Transcript;
import com.example.resultmanagementsystem.services.ReportCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report-cards")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Report Card & Transcript Management", description = "APIs for generating and managing student report cards and academic transcripts")
public class ReportCardController {

    private final ReportCardService reportCardService;

    @Autowired
    public ReportCardController(ReportCardService reportCardService) {
        this.reportCardService = reportCardService;
    }

    // ===== Report Card Endpoints =====

    @PostMapping("/generate")
    @Operation(summary = "Generate a report card", description = "Generates a report card for a specific student, academic year, and semester. Pulls results, calculates GPA, and determines class rank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report card generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportCard.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Student or results not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ReportCard> generateReportCard(
            @Parameter(description = "Report card request containing student ID, academic year, and semester", required = true)
            @Valid @RequestBody ReportCardRequestDTO requestDTO) {
        ReportCard reportCard = reportCardService.generateReportCard(requestDTO);
        return new ResponseEntity<>(reportCard, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get report card by ID", description = "Retrieves a specific report card using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report card retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportCard.class))),
            @ApiResponse(responseCode = "404", description = "Report card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ReportCard> getReportCard(
            @Parameter(description = "Unique identifier of the report card", required = true)
            @PathVariable String id) {
        ReportCard reportCard = reportCardService.getReportCard(id);
        return ResponseEntity.ok(reportCard);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get report cards by student", description = "Retrieves all report cards for a specific student across all semesters and academic years.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report cards retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportCard.class)))
    })
    public ResponseEntity<List<ReportCard>> getReportCardsByStudent(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        List<ReportCard> reportCards = reportCardService.getReportCardsByStudent(studentId);
        return ResponseEntity.ok(reportCards);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get report cards by department", description = "Retrieves all report cards for a specific department, filtered by academic year and semester.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report cards retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportCard.class))),
            @ApiResponse(responseCode = "400", description = "Missing required parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<ReportCard>> getReportCardsByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId,
            @Parameter(description = "Academic year", required = true, example = "2025")
            @RequestParam String academicYear,
            @Parameter(description = "Semester", required = true, example = "FIRST")
            @RequestParam String semester) {
        List<ReportCard> reportCards = reportCardService.getReportCardsByDepartment(departmentId, academicYear, semester);
        return ResponseEntity.ok(reportCards);
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish a report card", description = "Changes the status of a report card from DRAFT to PUBLISHED, making it visible to students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report card published successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportCard.class))),
            @ApiResponse(responseCode = "404", description = "Report card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ReportCard> publishReportCard(
            @Parameter(description = "Unique identifier of the report card", required = true)
            @PathVariable String id) {
        ReportCard reportCard = reportCardService.publishReportCard(id);
        return ResponseEntity.ok(reportCard);
    }

    @PostMapping("/generate/bulk")
    @Operation(summary = "Generate bulk report cards", description = "Generates report cards for all students in a department for a specific academic year and semester.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bulk report cards generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportCard.class))),
            @ApiResponse(responseCode = "400", description = "Missing required parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<ReportCard>> generateBulkReportCards(
            @Parameter(description = "Unique identifier of the department", required = true)
            @RequestParam String departmentId,
            @Parameter(description = "Academic year", required = true, example = "2025")
            @RequestParam String academicYear,
            @Parameter(description = "Semester", required = true, example = "FIRST")
            @RequestParam String semester) {
        List<ReportCard> reportCards = reportCardService.generateBulkReportCards(departmentId, academicYear, semester);
        return new ResponseEntity<>(reportCards, HttpStatus.CREATED);
    }

    // ===== Transcript Endpoints =====

    @PostMapping("/transcript/generate")
    @Operation(summary = "Generate a transcript", description = "Generates a complete academic transcript for a student, pulling results from all semesters and calculating cumulative GPA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transcript generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transcript.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Student or results not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Transcript> generateTranscript(
            @Parameter(description = "Transcript request containing student ID", required = true)
            @Valid @RequestBody TranscriptRequestDTO requestDTO) {
        Transcript transcript = reportCardService.generateTranscript(requestDTO);
        return new ResponseEntity<>(transcript, HttpStatus.CREATED);
    }

    @GetMapping("/transcript/{id}")
    @Operation(summary = "Get transcript by ID", description = "Retrieves a specific academic transcript using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transcript retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transcript.class))),
            @ApiResponse(responseCode = "404", description = "Transcript not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Transcript> getTranscript(
            @Parameter(description = "Unique identifier of the transcript", required = true)
            @PathVariable String id) {
        Transcript transcript = reportCardService.getTranscript(id);
        return ResponseEntity.ok(transcript);
    }

    @GetMapping("/transcript/verify/{verificationCode}")
    @Operation(summary = "Verify a transcript", description = "Verifies the authenticity of a transcript using its unique verification code (UUID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transcript verified successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transcript.class))),
            @ApiResponse(responseCode = "404", description = "Transcript not found with the given verification code",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Transcript> verifyTranscript(
            @Parameter(description = "Verification code (UUID) of the transcript", required = true)
            @PathVariable String verificationCode) {
        Transcript transcript = reportCardService.verifyTranscript(verificationCode);
        return ResponseEntity.ok(transcript);
    }

    // ===== Exception Handlers =====

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation Error");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
