package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.userdto.RegisterRequest;
import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.services.AuthenticationService;
import com.example.resultmanagementsystem.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Student Management", description = "APIs for managing student records including registration, retrieval, updates, and deletion. Also handles authentication integration for student accounts.")
public class StudentController {

    private final StudentService studentService;
    private final AuthenticationService authenticationService;

    @PostMapping("/createStudent")
    @Operation(
            summary = "Create a new student record",
            description = "Creates a new student record in the system and automatically registers them for authentication. Generates a unique student ID and sets up the student's account with default permissions."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Student created successfully and registered for authentication",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data - validation errors in student information, missing required fields, or malformed JSON",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - Student with this email or other unique identifier already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred during student creation or authentication setup",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Student> createStudent(
            @Parameter(description = "Student object containing all necessary student information including name, email, department, etc.", required = true)
            @RequestBody Student student
    ) {
        Student createdStudent = studentService.createStudent(student);

        // Register the student for authentication
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname(student.getName().split(" ")[0])  // Assuming name is in "firstname lastname" format
                .lastname(student.getName().contains(" ") ?
                        student.getName().substring(student.getName().indexOf(" ") + 1) : "")
                .email(student.getEmail())
                .role("STUDENT")  // Set role explicitly
                .build();

        // Register the student with auth service
        authenticationService.registerStudent(registerRequest, createdStudent.getStudentId());

        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    @Operation(
            summary = "Get student by ID",
            description = "Retrieves a specific student record using their unique student identifier. Returns complete student information including personal details, department affiliation, and enrollment status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid student ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving student information",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId
    ) {
        Student student = studentService.getStudentById(studentId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get students by department",
            description = "Retrieves all students enrolled in a specific department. Useful for departmental reporting, course planning, and administrative tasks."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid department ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No students found in the specified department or department does not exist",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving department students",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Student>> getStudentsByDepartmentId(
            @Parameter(description = "Unique identifier of the department", required = true, example = "DEPT001")
            @PathVariable String departmentId
    ) {
        List<Student> students = studentService.getStudentByDepartmentId(departmentId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all students",
            description = "Retrieves a complete list of all students registered in the system. This endpoint supports pagination and filtering for large datasets."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving student list",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    @Operation(
            summary = "Delete a student record",
            description = "Permanently removes a student record from the system. This action also removes associated authentication credentials and may affect related academic records. Use with caution as this operation cannot be undone."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid student ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cannot delete student due to existing academic records or other dependencies",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while deleting the student",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, String>> deleteStudent(
            @Parameter(description = "Unique identifier of the student to delete", required = true, example = "STU001")
            @PathVariable String studentId
    ) {
        studentService.deleteStudent(studentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student successfully deleted.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{studentId}/name")
    @Operation(
            summary = "Get student name by ID",
            description = "Retrieves only the name of a specific student using their unique identifier. Useful for display purposes where full student details are not required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student name retrieved successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "John Doe"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid student ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving student name",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<String> getStudentNameById(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId
    ) {
        String studentName = studentService.getStudentNameById(studentId);
        return new ResponseEntity<>(studentName, HttpStatus.OK);
    }

    @GetMapping("/{studentId}/email")
    @Operation(
            summary = "Get student email by ID",
            description = "Retrieves only the email address of a specific student using their unique identifier. Useful for communication and notification purposes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student email retrieved successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "john.doe@university.edu"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid student ID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found with the specified ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving student email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<String> getStudentEmailById(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId
    ) {
        String studentEmail = studentService.getStudentEmailById(studentId);
        return new ResponseEntity<>(studentEmail, HttpStatus.OK);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Get student by email address",
            description = "Retrieves a student record using their email address. This is useful for authentication flows and when the student ID is not readily available."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email format or missing email parameter",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found with the specified email address",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred while retrieving student by email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Student> getStudentByEmail(
            @Parameter(description = "Email address of the student", required = true, example = "john.doe@university.edu")
            @RequestParam String email
    ) {
        Student student = studentService.getStudentIdByEmail(email);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    /**
     * Exception handler for RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}