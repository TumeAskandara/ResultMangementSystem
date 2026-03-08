package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ExamDTO;
import com.example.resultmanagementsystem.Dto.ExamResultDTO;
import com.example.resultmanagementsystem.Dto.ExamScheduleDTO;
import com.example.resultmanagementsystem.Dto.QuestionBankDTO;
import com.example.resultmanagementsystem.model.Exam;
import com.example.resultmanagementsystem.model.ExamResult;
import com.example.resultmanagementsystem.model.ExamSchedule;
import com.example.resultmanagementsystem.model.QuestionBank;
import com.example.resultmanagementsystem.services.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Examination Management", description = "APIs for managing examinations, question banks, exam results, and exam schedules")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    // ===== Exam Endpoints =====

    @PostMapping
    @Operation(summary = "Create a new exam", description = "Creates a new examination with the provided details including course, department, schedule, and marking criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exam created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Exam> createExam(
            @Parameter(description = "Exam data transfer object", required = true)
            @Valid @RequestBody ExamDTO examDTO) {
        Exam exam = examService.createExam(examDTO);
        return new ResponseEntity<>(exam, HttpStatus.CREATED);
    }

    @GetMapping("/{examId}")
    @Operation(summary = "Get exam by ID", description = "Retrieves a specific examination using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exam retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class))),
            @ApiResponse(responseCode = "404", description = "Exam not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Exam> getExamById(
            @Parameter(description = "Unique identifier of the exam", required = true, example = "exam-uuid-123")
            @PathVariable String examId) {
        Exam exam = examService.getExamById(examId);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get exams by course", description = "Retrieves all examinations associated with a specific course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exams retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class)))
    })
    public ResponseEntity<List<Exam>> getExamsByCourse(
            @Parameter(description = "Unique identifier of the course", required = true)
            @PathVariable String courseId) {
        List<Exam> exams = examService.getExamsByCourse(courseId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get exams by department", description = "Retrieves all examinations associated with a specific department.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exams retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class)))
    })
    public ResponseEntity<List<Exam>> getExamsByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId) {
        List<Exam> exams = examService.getExamsByDepartment(departmentId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get exams by teacher", description = "Retrieves all examinations created by a specific teacher.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exams retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class)))
    })
    public ResponseEntity<List<Exam>> getExamsByTeacher(
            @Parameter(description = "Unique identifier of the teacher", required = true)
            @PathVariable String teacherId) {
        List<Exam> exams = examService.getExamsByTeacher(teacherId);
        return ResponseEntity.ok(exams);
    }

    @PatchMapping("/{examId}/status")
    @Operation(summary = "Update exam status", description = "Updates the status of an examination (e.g., SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, POSTPONED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exam status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class))),
            @ApiResponse(responseCode = "404", description = "Exam not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Exam> updateExamStatus(
            @Parameter(description = "Unique identifier of the exam", required = true)
            @PathVariable String examId,
            @Parameter(description = "New status for the exam", required = true)
            @RequestParam Exam.ExamStatus status) {
        Exam exam = examService.updateExamStatus(examId, status);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming exams", description = "Retrieves all examinations scheduled in the future, ordered by start date ascending.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upcoming exams retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exam.class)))
    })
    public ResponseEntity<List<Exam>> getUpcomingExams() {
        List<Exam> exams = examService.getUpcomingExams();
        return ResponseEntity.ok(exams);
    }

    // ===== Question Bank Endpoints =====

    @PostMapping("/questions")
    @Operation(summary = "Add a question to the question bank", description = "Creates a new question in the question bank with details such as question text, options, correct answer, difficulty, and marks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionBank.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<QuestionBank> addQuestion(
            @Parameter(description = "Question bank data transfer object", required = true)
            @Valid @RequestBody QuestionBankDTO questionBankDTO) {
        QuestionBank question = examService.addQuestion(questionBankDTO);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @GetMapping("/questions/course/{courseId}")
    @Operation(summary = "Get questions by course", description = "Retrieves all questions in the question bank for a specific course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionBank.class)))
    })
    public ResponseEntity<List<QuestionBank>> getQuestionsByCourse(
            @Parameter(description = "Unique identifier of the course", required = true)
            @PathVariable String courseId) {
        List<QuestionBank> questions = examService.getQuestionsByCourse(courseId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/generate")
    @Operation(summary = "Generate random questions", description = "Generates a random set of questions from the question bank for a specific course, with optional difficulty filtering.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random questions generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionBank.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<QuestionBank>> generateRandomQuestions(
            @Parameter(description = "Unique identifier of the course", required = true)
            @RequestParam String courseId,
            @Parameter(description = "Number of questions to generate", required = true, example = "10")
            @RequestParam int count,
            @Parameter(description = "Difficulty level filter (EASY, MEDIUM, HARD)", required = false)
            @RequestParam(required = false) QuestionBank.Difficulty difficulty) {
        List<QuestionBank> questions = examService.generateRandomQuestions(courseId, count, difficulty);
        return ResponseEntity.ok(questions);
    }

    // ===== Exam Result Endpoints =====

    @PostMapping("/results")
    @Operation(summary = "Submit exam result", description = "Submits an exam result for a student including answers, scores, and grades. Automatically calculates percentage and determines pass/fail status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exam result submitted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Exam not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ExamResult> submitExamResult(
            @Parameter(description = "Exam result data transfer object", required = true)
            @Valid @RequestBody ExamResultDTO examResultDTO) {
        ExamResult result = examService.submitExamResult(examResultDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/results/exam/{examId}")
    @Operation(summary = "Get results by exam", description = "Retrieves all exam results for a specific examination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exam results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamResult.class)))
    })
    public ResponseEntity<List<ExamResult>> getExamResults(
            @Parameter(description = "Unique identifier of the exam", required = true)
            @PathVariable String examId) {
        List<ExamResult> results = examService.getExamResults(examId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/student/{studentId}")
    @Operation(summary = "Get results by student", description = "Retrieves all exam results for a specific student across all examinations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student exam results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamResult.class)))
    })
    public ResponseEntity<List<ExamResult>> getStudentExamResults(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        List<ExamResult> results = examService.getStudentExamResults(studentId);
        return ResponseEntity.ok(results);
    }

    // ===== Exam Schedule Endpoints =====

    @PostMapping("/schedule")
    @Operation(summary = "Create exam schedule", description = "Creates a new exam schedule entry with venue, date, time, and invigilator details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exam schedule created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamSchedule.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ExamSchedule> createExamSchedule(
            @Parameter(description = "Exam schedule data transfer object", required = true)
            @Valid @RequestBody ExamScheduleDTO examScheduleDTO) {
        ExamSchedule schedule = examService.createExamSchedule(examScheduleDTO);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @GetMapping("/schedule/department/{departmentId}")
    @Operation(summary = "Get schedule by department", description = "Retrieves all exam schedules for a specific department.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exam schedules retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamSchedule.class)))
    })
    public ResponseEntity<List<ExamSchedule>> getScheduleByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId) {
        List<ExamSchedule> schedules = examService.getScheduleByDepartment(departmentId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedule/date/{date}")
    @Operation(summary = "Get schedule by date", description = "Retrieves all exam schedules for a specific date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exam schedules retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamSchedule.class)))
    })
    public ResponseEntity<List<ExamSchedule>> getScheduleByDate(
            @Parameter(description = "Date for schedule lookup (format: yyyy-MM-dd)", required = true, example = "2025-06-15")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ExamSchedule> schedules = examService.getScheduleByDate(date);
        return ResponseEntity.ok(schedules);
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
