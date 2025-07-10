package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.model.Assignment;
import com.example.resultmanagementsystem.services.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO> createAssignment(
            @PathVariable String teacherId,
            @Valid @RequestBody AssignmentCreateDTO dto) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Assignment assignment = assignmentService.createAssignment(teacherId, dto);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusDescription("CREATED")
                            .message("Assignment created successfully")
                            .build())
                    .data(assignment)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating assignment: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to create assignment")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponseDTO> getAssignmentsForStudent(
            @PathVariable String studentId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<AssignmentResponseDTO> assignments = assignmentService.getAssignmentsForStudent(studentId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Assignments retrieved successfully")
                            .build())
                    .data(assignments)
                    .paginationDto(PaginationDTO.builder()
                            .count(assignments.size())
                            .total(assignments.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving assignments for student {}: {}", studentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Assignments not found for student")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO> getAssignmentsForTeacher(
            @PathVariable String teacherId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Assignment> assignments = assignmentService.getAssignmentsForTeacher(teacherId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Assignments retrieved successfully")
                            .build())
                    .data(assignments)
                    .paginationDto(PaginationDTO.builder()
                            .count(assignments.size())
                            .total(assignments.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving assignments for teacher {}: {}", teacherId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Assignments not found for teacher")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<ResponseDTO> getAssignmentById(@PathVariable String assignmentId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Assignment assignment = assignmentService.getAssignmentById(assignmentId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Assignment retrieved successfully")
                            .build())
                    .data(assignment)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving assignment {}: {}", assignmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Assignment not found")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{assignmentId}/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO> updateAssignment(
            @PathVariable String assignmentId,
            @PathVariable String teacherId,
            @Valid @RequestBody AssignmentCreateDTO dto) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Assignment assignment = assignmentService.updateAssignment(assignmentId, teacherId, dto);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Assignment updated successfully")
                            .build())
                    .data(assignment)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating assignment {}: {}", assignmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to update assignment")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{assignmentId}/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO> deleteAssignment(
            @PathVariable String assignmentId,
            @PathVariable String teacherId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            assignmentService.deleteAssignment(assignmentId, teacherId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .statusDescription("NO_CONTENT")
                            .message("Assignment deleted successfully")
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting assignment {}: {}", assignmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to delete assignment")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}