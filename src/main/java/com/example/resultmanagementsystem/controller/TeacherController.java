package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ResponseDTO;
import com.example.resultmanagementsystem.Dto.MetaDTO;
import com.example.resultmanagementsystem.Dto.TeacherDTO;
import com.example.resultmanagementsystem.model.Teacher;
import com.example.resultmanagementsystem.services.TeacherService;
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
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teacher Management", description = "APIs for managing teacher profiles, including CRUD operations for teacher data")
public class TeacherController {

    private final TeacherService teacherService;



    @GetMapping("/getTeacherByemail/{email}")
    @Operation(
            summary = "Get Teacher by Email",
            description = "Retrieves a teacher's profile using their email address. This is useful for fetching teacher details for various operations."
    )

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Teacher found",
            content = @Content(schema = @Schema(implementation = TeacherDTO.class))), @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(schema = @Schema(implementation = ResponseDTO.class))), @ApiResponse(responseCode = "201", description = "Teacher created successfully",
            content = @Content(schema = @Schema(implementation = ResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Invalid teacher data or validation errors",
            content = @Content(schema = @Schema(implementation = ResponseDTO.class))), @ApiResponse(responseCode = "409", description = "Teacher with this email already exists",
            content = @Content(schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> getTeacherByEmail(
            @Parameter(description = "Email of the teacher to retrieve", required = true)
            @PathVariable String email) {

        Teacher teacher = teacherService.getTeacherByEmail(email);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("OK")
                        .message("Teacher found successfully")
                        .build())
                .data(teacher)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }
    @PostMapping("/createTeacher")
    @Operation(
            summary = "Create Teacher",
            description = "Creates a new teacher profile in the system. This endpoint is typically used by administrators to add new teachers."
    )

    public ResponseEntity<ResponseDTO> createTeacher(
            @Parameter(description = "Teacher information including name, email, department, and other details", required = true)
            @Valid @RequestBody Teacher teacher) {

        Teacher createdTeacher = teacherService.createTeacher(teacher);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .statusDescription("CREATED")
                        .message("Teacher created successfully")
                        .build())
                .data(createdTeacher)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getTeacherById/{id}")
    @Operation(
            summary = "Get Teacher by ID",
            description = "Retrieves detailed information about a specific teacher using their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found with the specified ID",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getTeacherById(
            @Parameter(description = "Unique identifier of the teacher", required = true)
            @PathVariable String id) {

        Teacher teacher = teacherService.getTeacherById(id);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Teacher retrieved successfully")
                        .build())
                .data(teacher)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllTeachers")
    @Operation(
            summary = "Get All Teachers",
            description = "Retrieves a list of all teachers in the system. This endpoint is typically used by administrators for teacher management."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> getAllTeachers() {

        List<Teacher> teachers = teacherService.getAllTeachers();

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("All teachers retrieved successfully")
                        .build())
                .data(teachers)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateTeacher/{id}")
    @Operation(
            summary = "Update Teacher Information",
            description = "Updates an existing teacher's information. Only the provided fields in the TeacherDTO will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data or validation errors",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found with the specified ID",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> updateTeacher(
            @Parameter(description = "Unique identifier of the teacher to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated teacher information", required = true)
            @Valid @RequestBody TeacherDTO teacherDTO) {

        Teacher updatedTeacher = teacherService.updateTeacher(id, teacherDTO);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Teacher updated successfully")
                        .build())
                .data(updatedTeacher)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteTeacher/{id}")
    @Operation(
            summary = "Delete Teacher",
            description = "Permanently removes a teacher from the system. This action cannot be undone and should be used with caution."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found with the specified ID",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Cannot delete teacher due to existing dependencies",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> deleteTeacher(
            @Parameter(description = "Unique identifier of the teacher to delete", required = true)
            @PathVariable String id) {

        teacherService.deleteTeacher(id);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Teacher deleted successfully")
                        .build())
                .data("Teacher with ID " + id + " has been successfully deleted")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}