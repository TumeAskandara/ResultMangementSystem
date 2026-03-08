package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.model.AnalyticsReport;
import com.example.resultmanagementsystem.services.AnalyticsService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Analytics & Reporting", description = "APIs for generating analytics reports, viewing student performance metrics, department statistics, fee collection data, attendance trends, enrollment trends, exam analysis, and system-wide dashboard statistics.")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/student/{studentId}/performance")
    @Operation(
            summary = "Get student performance analytics",
            description = "Retrieves comprehensive performance analytics for a specific student including semester GPAs, cumulative GPA, attendance percentage, and performance trend."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student performance analytics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentPerformanceAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StudentPerformanceAnalyticsDTO> getStudentPerformanceAnalytics(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        StudentPerformanceAnalyticsDTO analytics = analyticsService.getStudentPerformanceAnalytics(studentId);
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get department analytics",
            description = "Retrieves analytics data for a specific department including total students, average GPA, pass rate, top performers, and enrollment count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department analytics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DepartmentAnalyticsDTO> getDepartmentAnalytics(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId,
            @Parameter(description = "Academic year filter")
            @RequestParam(required = false) String academicYear) {
        DepartmentAnalyticsDTO analytics = analyticsService.getDepartmentAnalytics(departmentId, academicYear);
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }

    @GetMapping("/fees")
    @Operation(
            summary = "Get fee analytics",
            description = "Retrieves fee collection analytics including total expected, total collected, outstanding amount, collection rate, and department-wise breakdown."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee analytics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FeeAnalyticsDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FeeAnalyticsDTO> getFeeAnalytics(
            @Parameter(description = "Academic year filter")
            @RequestParam(required = false) String academicYear) {
        FeeAnalyticsDTO analytics = analyticsService.getFeeAnalytics(academicYear);
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }

    @GetMapping("/attendance-trend")
    @Operation(
            summary = "Get attendance trend",
            description = "Retrieves attendance trend data for a specific department, academic year, and semester."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance trend data retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getAttendanceTrend(
            @Parameter(description = "Department ID") @RequestParam(required = false) String departmentId,
            @Parameter(description = "Academic year") @RequestParam(required = false) String academicYear,
            @Parameter(description = "Semester") @RequestParam(required = false) String semester) {
        Map<String, Object> trend = analyticsService.getAttendanceTrend(departmentId, academicYear, semester);
        return new ResponseEntity<>(trend, HttpStatus.OK);
    }

    @GetMapping("/enrollment-trend")
    @Operation(
            summary = "Get enrollment trend",
            description = "Retrieves enrollment trend data for a specific academic year including breakdown by status and department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment trend data retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getEnrollmentTrend(
            @Parameter(description = "Academic year") @RequestParam(required = false) String academicYear) {
        Map<String, Object> trend = analyticsService.getEnrollmentTrend(academicYear);
        return new ResponseEntity<>(trend, HttpStatus.OK);
    }

    @GetMapping("/exam/{examId}/analysis")
    @Operation(
            summary = "Get exam analysis",
            description = "Retrieves detailed analysis of a specific exam including average score, pass rate, grade distribution, and score statistics."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exam analysis retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Exam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getExamAnalysis(
            @Parameter(description = "Unique identifier of the exam", required = true)
            @PathVariable String examId) {
        Map<String, Object> analysis = analyticsService.getExamAnalysis(examId);
        return new ResponseEntity<>(analysis, HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    @Operation(
            summary = "Get dashboard statistics",
            description = "Retrieves system-wide dashboard statistics including total students, teachers, courses, departments, active enrollments, pending items, fee collection rate, and overall pass rate."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardStatsDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = analyticsService.getDashboardStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PostMapping("/reports/generate")
    @Operation(
            summary = "Generate a report",
            description = "Generates and saves an analytics report based on the provided parameters. Supports various report types including student performance, attendance trend, fee collection, enrollment trend, department comparison, and exam analysis."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalyticsReport.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AnalyticsReport> generateReport(
            @Parameter(description = "Analytics request parameters", required = true)
            @RequestBody AnalyticsRequestDTO request) {
        AnalyticsReport report = analyticsService.generateReport(request);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @GetMapping("/reports/{id}")
    @Operation(
            summary = "Get report by ID",
            description = "Retrieves a previously generated analytics report by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalyticsReport.class))),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AnalyticsReport> getReportById(
            @Parameter(description = "Unique identifier of the report", required = true)
            @PathVariable String id) {
        AnalyticsReport report = analyticsService.getReportById(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/reports/type/{reportType}")
    @Operation(
            summary = "Get reports by type",
            description = "Retrieves paginated list of reports filtered by report type."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid report type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AnalyticsReport>> getReportsByType(
            @Parameter(description = "Type of report", required = true)
            @PathVariable String reportType,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AnalyticsReport> reports = analyticsService.getReportsByType(reportType, pageable);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
