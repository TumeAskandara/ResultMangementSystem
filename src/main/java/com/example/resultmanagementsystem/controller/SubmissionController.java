package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ResponseDTO;
import com.example.resultmanagementsystem.Dto.MetaDTO;
import com.example.resultmanagementsystem.Dto.SubmissionCreateDTO;
import com.example.resultmanagementsystem.model.Submission;
import com.example.resultmanagementsystem.services.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Submission Management", description = "APIs for managing student assignment submissions, grading, and retrieval")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/student/{studentId}")
    @Operation(
            summary = "Submit Assignment",
            description = "Allows a student to submit an assignment. The submission includes content, assignment reference, and is automatically timestamped."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assignment submitted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid submission data or validation errors",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student or assignment not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> submitAssignment(
            @Parameter(description = "Unique identifier of the student submitting the assignment", required = true)
            @PathVariable String studentId,
            @Parameter(description = "Submission details including assignment ID and content", required = true)
            @Valid @RequestBody SubmissionCreateDTO dto) {

        Submission submission = submissionService.submitAssignment(studentId, dto);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .statusDescription("CREATED")
                        .message("Assignment submitted successfully")
                        .build())
                .data(submission)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/department/teacher/{teacherId}")
    @Operation(
            summary = "Get All Department Submissions",
            description = "Retrieves all submissions from students in the teacher's department. Teachers can only access submissions from their own department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - teacher not authorized for this department",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getSubmissionsForDepartment(
            @Parameter(description = "Unique identifier of the teacher requesting submissions", required = true)
            @PathVariable String teacherId) {

        List<Submission> submissions = submissionService.getSubmissionsForDepartment(teacherId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Department submissions retrieved successfully")
                        .build())
                .data(submissions)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/teacher/{teacherId}/ungraded")
    @Operation(
            summary = "Get Ungraded Department Submissions",
            description = "Retrieves only ungraded submissions from students in the teacher's department. Useful for teachers to see pending grading work."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ungraded submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - teacher not authorized for this department",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getUngradedSubmissionsForDepartment(
            @Parameter(description = "Unique identifier of the teacher requesting ungraded submissions", required = true)
            @PathVariable String teacherId) {

        List<Submission> submissions = submissionService.getUngradedSubmissionsForDepartment(teacherId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Ungraded submissions retrieved successfully")
                        .build())
                .data(submissions)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/teacher/{teacherId}/graded")
    @Operation(
            summary = "Get Graded Department Submissions",
            description = "Retrieves only graded submissions from students in the teacher's department. Useful for reviewing completed grading work."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Graded submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - teacher not authorized for this department",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getGradedSubmissionsForDepartment(
            @Parameter(description = "Unique identifier of the teacher requesting graded submissions", required = true)
            @PathVariable String teacherId) {

        List<Submission> submissions = submissionService.getGradedSubmissionsForDepartment(teacherId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Graded submissions retrieved successfully")
                        .build())
                .data(submissions)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignment/{assignmentId}/teacher/{teacherId}")
    @Operation(
            summary = "Get Submissions for Specific Assignment",
            description = "Retrieves all submissions for a specific assignment. Access is department-based - teachers can only see submissions from their department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - teacher not authorized for this assignment",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Assignment or teacher not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getSubmissionsForAssignment(
            @Parameter(description = "Unique identifier of the assignment", required = true)
            @PathVariable String assignmentId,
            @Parameter(description = "Unique identifier of the teacher requesting submissions", required = true)
            @PathVariable String teacherId) {

        List<Submission> submissions = submissionService.getSubmissionsForAssignment(assignmentId, teacherId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Assignment submissions retrieved successfully")
                        .build())
                .data(submissions)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    @Operation(
            summary = "Get Student's Submissions",
            description = "Retrieves all submissions made by a specific student across all assignments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student not found or no submissions found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getSubmissionsForStudent(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {

        List<Submission> submissions = submissionService.getSubmissionsForStudent(studentId);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Student submissions retrieved successfully")
                        .build())
                .data(submissions)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{submissionId}/grade/teacher/{teacherId}")
    @Operation(
            summary = "Grade Single Submission",
            description = "Allows a teacher to grade a single submission. Access is department-based - teachers can only grade submissions from their department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission graded successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid grade value or grading parameters",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - teacher not authorized to grade this submission",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Submission or teacher not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> gradeSubmission(
            @Parameter(description = "Unique identifier of the submission to grade", required = true)
            @PathVariable String submissionId,
            @Parameter(description = "Unique identifier of the teacher grading the submission", required = true)
            @PathVariable String teacherId,
            @Parameter(description = "Grade to assign (typically 0-100)", required = true)
            @RequestParam Integer grade,
            @Parameter(description = "Optional feedback comments for the student")
            @RequestParam(required = false) String feedback) {

        Submission submission = submissionService.gradeSubmission(submissionId, teacherId, grade, feedback);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Submission graded successfully")
                        .build())
                .data(submission)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/batch-grade/teacher/{teacherId}")
    @Operation(
            summary = "Batch Grade Multiple Submissions",
            description = "Allows a teacher to grade multiple submissions at once. Each submission in the batch must include submissionId, grade, and optional feedback."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch grading completed successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid batch grading data or validation errors",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - teacher not authorized to grade one or more submissions",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found or one or more submissions not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> batchGradeSubmissions(
            @Parameter(description = "Unique identifier of the teacher performing batch grading", required = true)
            @PathVariable String teacherId,
            @Parameter(description = "List of grading data containing submissionId, grade, and optional feedback for each submission", required = true)
            @RequestBody List<Map<String, Object>> gradingData) {

        List<Submission> gradedSubmissions = gradingData.stream()
                .map(data -> submissionService.gradeSubmission(
                        (String) data.get("submissionId"),
                        teacherId,
                        (Integer) data.get("grade"),
                        (String) data.get("feedback")
                ))
                .toList();

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Batch grading completed successfully")
                        .build())
                .data(gradedSubmissions)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignment/{assignmentId}/student/{studentId}")
    @Operation(
            summary = "Get Specific Student's Assignment Submission",
            description = "Retrieves a specific submission by a student for a particular assignment. Returns the submission if it exists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission found and retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found for the specified assignment and student",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getSubmissionByAssignmentAndStudent(
            @Parameter(description = "Unique identifier of the assignment", required = true)
            @PathVariable String assignmentId,
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {

        Submission submission = submissionService.getSubmissionByAssignmentAndStudent(assignmentId, studentId);

        if (submission != null) {
            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("SUCCESS")
                            .message("Submission retrieved successfully")
                            .build())
                    .data(submission)
                    .correlationId(UUID.randomUUID().toString())
                    .transactionId(UUID.randomUUID().toString())
                    .build();

            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Submission not found for assignment " + assignmentId + " and student " + studentId);
        }
    }
}