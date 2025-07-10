package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.Dto.complainDTO.ComplaintAssignmentDTO;
import com.example.resultmanagementsystem.Dto.complainDTO.ComplaintDTO;
import com.example.resultmanagementsystem.Dto.complainDTO.ComplaintDashboardDTO;
import com.example.resultmanagementsystem.Dto.complainDTO.ComplaintDetailDTO;
import com.example.resultmanagementsystem.model.Complaint;
import com.example.resultmanagementsystem.services.ComplaintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class ComplaintController {

    private final ComplaintService complaintService;

    /**
     * Create a new complaint
     */
    @PostMapping
    public ResponseEntity<ResponseDTO> createComplaint(@RequestBody ComplaintDTO complaintDTO) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Complaint createdComplaint = complaintService.createComplaint(complaintDTO);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusDescription("CREATED")
                            .message("Complaint created successfully")
                            .build())
                    .data(createdComplaint)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating complaint: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to create complaint")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get complaint by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getComplaintById(@PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Complaint complaint = complaintService.getComplaintById(id);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaint retrieved successfully")
                            .build())
                    .data(complaint)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving complaint {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Complaint not found")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get detailed complaint information by ID
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<ResponseDTO> getComplaintDetailsById(@PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            ComplaintDetailDTO complaintDetail = complaintService.getComplaintDetailsById(id);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaint details retrieved successfully")
                            .build())
                    .data(complaintDetail)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving complaint details {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Complaint details not found")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get complaints by student ID
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponseDTO> getComplaintsByStudentId(@PathVariable String studentId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Complaint> complaints = complaintService.getComplaintsByStudentId(studentId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaints retrieved successfully")
                            .build())
                    .data(complaints)
                    .paginationDto(PaginationDTO.builder()
                            .count(complaints.size())
                            .total(complaints.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving complaints for student {}: {}", studentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Complaints not found for student")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get complaints by course ID
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO> getComplaintsByCourseId(@PathVariable String courseId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Complaint> complaints = complaintService.getComplaintsByCourseId(courseId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaints retrieved successfully")
                            .build())
                    .data(complaints)
                    .paginationDto(PaginationDTO.builder()
                            .count(complaints.size())
                            .total(complaints.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving complaints for course {}: {}", courseId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Complaints not found for course")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get complaints by status with pagination
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDTO> getComplaintsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
                    Sort.Direction.ASC : Sort.Direction.DESC;

            Complaint.ComplaintStatus complaintStatus = Complaint.ComplaintStatus.valueOf(status);

            Page<Complaint> complaints = complaintService.getComplaintsByStatus(
                    complaintStatus,
                    PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
            );

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaints retrieved successfully")
                            .build())
                    .data(complaints.getContent())
                    .paginationDto(PaginationDTO.builder()
                            .count((int) complaints.getNumberOfElements())
                            .total((int) complaints.getTotalElements())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving complaints by status {}: {}", status, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to retrieve complaints by status")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get complaints assigned to a specific teacher
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO> getComplaintsByTeacher(@PathVariable String teacherId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Complaint> complaints = complaintService.getComplaintsByTeacher(teacherId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaints retrieved successfully")
                            .build())
                    .data(complaints)
                    .paginationDto(PaginationDTO.builder()
                            .count(complaints.size())
                            .total(complaints.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving complaints for teacher {}: {}", teacherId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Complaints not found for teacher")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get complaints assigned to a specific teacher with pagination
     */
    @GetMapping("/teacher/{teacherId}/paginated")
    public ResponseEntity<ResponseDTO> getComplaintsByTeacherPaginated(
            @PathVariable String teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
                    Sort.Direction.ASC : Sort.Direction.DESC;

            Page<Complaint> complaints = complaintService.getComplaintsByTeacher(
                    teacherId,
                    PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
            );

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaints retrieved successfully")
                            .build())
                    .data(complaints.getContent())
                    .paginationDto(PaginationDTO.builder()
                            .count((int) complaints.getNumberOfElements())
                            .total((int) complaints.getTotalElements())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving paginated complaints for teacher {}: {}", teacherId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Complaints not found for teacher")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get unresolved complaints assigned to a specific teacher
     */
    @GetMapping("/teacher/{teacherId}/unresolved")
    public ResponseEntity<ResponseDTO> getUnresolvedComplaintsByTeacher(@PathVariable String teacherId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Complaint> complaints = complaintService.getUnresolvedComplaintsByTeacher(teacherId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Unresolved complaints retrieved successfully")
                            .build())
                    .data(complaints)
                    .paginationDto(PaginationDTO.builder()
                            .count(complaints.size())
                            .total(complaints.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving unresolved complaints for teacher {}: {}", teacherId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .statusDescription("NOT_FOUND")
                            .message("Unresolved complaints not found for teacher")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update complaint status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateComplaintStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String response) {

        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Complaint updatedComplaint = complaintService.updateComplaintStatus(id, status, response);

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaint status updated successfully")
                            .build())
                    .data(updatedComplaint)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating complaint status {}: {}", id, e.getMessage());

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to update complaint status")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Assign complaint to a teacher
     */
    @PutMapping("/{id}/assign")
    public ResponseEntity<ResponseDTO> assignComplaint(
            @PathVariable String id,
            @RequestBody ComplaintAssignmentDTO assignmentDTO) {

        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Complaint assignedComplaint = complaintService.assignComplaint(id, assignmentDTO.getTeacherId());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaint assigned successfully")
                            .build())
                    .data(assignedComplaint)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error assigning complaint {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to assign complaint")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete complaint
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteComplaint(@PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            complaintService.deleteComplaint(id);

            Map<String, Boolean> deleteResponse = new HashMap<>();
            deleteResponse.put("deleted", Boolean.TRUE);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Complaint deleted successfully")
                            .build())
                    .data(deleteResponse)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting complaint {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to delete complaint")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get complaints dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ResponseDTO> getComplaintsDashboard() {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            ComplaintDashboardDTO dashboard = complaintService.getComplaintsDashboard();

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Dashboard statistics retrieved successfully")
                            .build())
                    .data(dashboard)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving dashboard statistics: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("Failed to retrieve dashboard statistics")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}