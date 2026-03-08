package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.AttendanceDTO;
import com.example.resultmanagementsystem.Dto.AttendanceReportDTO;
import com.example.resultmanagementsystem.Dto.BulkAttendanceDTO;
import com.example.resultmanagementsystem.model.Attendance;
import com.example.resultmanagementsystem.model.AttendanceSummary;
import com.example.resultmanagementsystem.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Attendance Management", description = "APIs for managing student attendance including marking, bulk operations, summaries, and reports")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(
            summary = "Mark single attendance",
            description = "Marks attendance for a single student for a specific course and date. Automatically updates the attendance summary."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Attendance marked successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Attendance.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Attendance> markAttendance(
            @Parameter(description = "Attendance details", required = true)
            @Valid @RequestBody AttendanceDTO attendanceDTO,
            @Parameter(description = "ID of the user marking attendance")
            @RequestParam(defaultValue = "system") String markedBy
    ) {
        Attendance attendance = attendanceService.markAttendance(attendanceDTO, markedBy);
        return new ResponseEntity<>(attendance, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    @Operation(
            summary = "Mark bulk attendance",
            description = "Marks attendance for multiple students in a class at once. Useful for teachers to mark attendance for an entire class session."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Bulk attendance marked successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Attendance>> markBulkAttendance(
            @Parameter(description = "Bulk attendance details including class, course, date, and individual student records", required = true)
            @Valid @RequestBody BulkAttendanceDTO bulkAttendanceDTO,
            @Parameter(description = "ID of the user marking attendance")
            @RequestParam(defaultValue = "system") String markedBy
    ) {
        List<Attendance> attendanceList = attendanceService.markBulkAttendance(bulkAttendanceDTO, markedBy);
        return new ResponseEntity<>(attendanceList, HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(
            summary = "Get attendance by student and course",
            description = "Retrieves all attendance records for a specific student in a specific course."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Attendance records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No records found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Attendance>> getAttendanceByStudentAndCourse(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId,
            @Parameter(description = "Course ID", required = true) @PathVariable String courseId
    ) {
        List<Attendance> records = attendanceService.getAttendanceByStudentAndCourse(studentId, courseId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/class/{classId}/date/{date}")
    @Operation(
            summary = "Get attendance by class and date",
            description = "Retrieves all attendance records for a specific class on a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Attendance records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Attendance>> getAttendanceByClassAndDate(
            @Parameter(description = "Class ID", required = true) @PathVariable String classId,
            @Parameter(description = "Date (yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Attendance> records = attendanceService.getAttendanceByClassAndDate(classId, date);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}/date/{date}")
    @Operation(
            summary = "Get attendance by course and date",
            description = "Retrieves all attendance records for a specific course on a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Attendance records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Attendance>> getAttendanceByCourseAndDate(
            @Parameter(description = "Course ID", required = true) @PathVariable String courseId,
            @Parameter(description = "Date (yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Attendance> records = attendanceService.getAttendanceByCourseAndDate(courseId, date);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}/summary")
    @Operation(
            summary = "Get student attendance summary",
            description = "Retrieves attendance summaries for a student filtered by academic year and semester."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Attendance summaries retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<AttendanceSummary>> getStudentAttendanceSummary(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId,
            @Parameter(description = "Academic year (e.g., 2025)") @RequestParam String academicYear,
            @Parameter(description = "Semester") @RequestParam String semester
    ) {
        List<AttendanceSummary> summaries = attendanceService.getStudentAttendanceSummary(studentId, academicYear, semester);
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}/report/{courseId}")
    @Operation(
            summary = "Get student attendance report",
            description = "Generates a detailed attendance report for a student in a specific course, including counts and percentage."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Attendance report generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttendanceReportDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<AttendanceReportDTO> getStudentAttendanceReport(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId,
            @Parameter(description = "Course ID", required = true) @PathVariable String courseId
    ) {
        AttendanceReportDTO report = attendanceService.getStudentAttendanceReport(studentId, courseId);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/low-attendance")
    @Operation(
            summary = "Get low attendance students",
            description = "Retrieves all students whose attendance percentage is below the specified threshold."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Low attendance students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<AttendanceSummary>> getLowAttendanceStudents(
            @Parameter(description = "Attendance percentage threshold (e.g., 75.0)", required = true)
            @RequestParam double threshold
    ) {
        List<AttendanceSummary> students = attendanceService.getLowAttendanceStudents(threshold);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}/range")
    @Operation(
            summary = "Get attendance by date range",
            description = "Retrieves attendance records for a student in a specific course within a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Attendance records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid date range",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Attendance>> getAttendanceByDateRange(
            @Parameter(description = "Student ID", required = true) @PathVariable String studentId,
            @Parameter(description = "Course ID", required = true) @RequestParam String courseId,
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Attendance> records = attendanceService.getAttendanceByDateRange(studentId, courseId, startDate, endDate);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
