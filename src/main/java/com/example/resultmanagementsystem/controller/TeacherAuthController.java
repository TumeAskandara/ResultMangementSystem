package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ResponseDTO;
import com.example.resultmanagementsystem.Dto.MetaDTO;
import com.example.resultmanagementsystem.model.Teacher;
import com.example.resultmanagementsystem.services.TeacherAuthService;
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

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/teachers/auth")
@RequiredArgsConstructor
@Tag(name = "Teacher Authentication", description = "APIs for teacher registration, login, and email verification")
public class TeacherAuthController {

    private final TeacherAuthService teacherAuthService;

    @PostMapping("/register")
    @Operation(
            summary = "Register New Teacher",
            description = "Registers a new teacher account in the system. This creates the teacher profile and sends a verification email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher registered successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid registration data or validation errors",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Teacher with this email already exists",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> registerTeacher(
            @Parameter(description = "Teacher registration information including email, password, and profile data", required = true)
            @Valid @RequestBody Teacher teacher) {

        Teacher registeredTeacher = teacherAuthService.registerTeacher(teacher);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .statusDescription("CREATED")
                        .message("Teacher registered successfully. Please check your email for verification.")
                        .build())
                .data(registeredTeacher)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Teacher Login",
            description = "Authenticates a teacher and returns a JWT token for accessing protected endpoints. The teacher must have a verified email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or unverified email",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher account not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> loginTeacher(
            @Parameter(description = "Teacher's registered email address", required = true)
            @RequestParam String email,
            @Parameter(description = "Teacher's password", required = true)
            @RequestParam String password) {

        String token = teacherAuthService.authenticateTeacher(email, password);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Login successful")
                        .build())
                .data(token)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    @Operation(
            summary = "Verify Teacher Email",
            description = "Verifies a teacher's email address using the verification token sent during registration. This activates the teacher account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired verification token",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Teacher account not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    public ResponseEntity<ResponseDTO> verifyEmail(
            @Parameter(description = "Email verification token sent to teacher's email address", required = true)
            @RequestParam String token) {

        String verificationResult = teacherAuthService.verifyTeacherEmail(token);

        ResponseDTO response = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusDescription("SUCCESS")
                        .message("Email verification completed")
                        .build())
                .data(verificationResult)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}