package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ClassPromotionDTO;
import com.example.resultmanagementsystem.Dto.ClassSectionDTO;
import com.example.resultmanagementsystem.Dto.ClassStatsDTO;
import com.example.resultmanagementsystem.model.ClassPromotion;
import com.example.resultmanagementsystem.model.ClassSection;
import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.services.ClassService;
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
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Class & Section Management", description = "APIs for managing class sections, student assignments, course assignments, student promotions, and class statistics.")
public class ClassController {

    private final ClassService classService;

    @PostMapping
    @Operation(
            summary = "Create a new class section",
            description = "Creates a new class section with specified details including class name, section, department, academic year, teacher assignment, and capacity."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Class section created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> createClass(
            @Parameter(description = "Class section details", required = true)
            @RequestBody ClassSectionDTO dto) {
        ClassSection classSection = classService.createClass(dto);
        return new ResponseEntity<>(classSection, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get class by ID",
            description = "Retrieves a specific class section by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Class section retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "404", description = "Class section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> getClassById(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String id) {
        ClassSection classSection = classService.getClassById(id);
        return new ResponseEntity<>(classSection, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get classes by department",
            description = "Retrieves all class sections for a specific department and academic year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classes retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ClassSection>> getClassesByDepartment(
            @Parameter(description = "Unique identifier of the department", required = true)
            @PathVariable String departmentId,
            @Parameter(description = "Academic year filter", required = true)
            @RequestParam String academicYear) {
        List<ClassSection> classes = classService.getClassesByDepartment(departmentId, academicYear);
        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(
            summary = "Get classes by teacher",
            description = "Retrieves all class sections assigned to a specific class teacher."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classes retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ClassSection>> getClassesByTeacher(
            @Parameter(description = "Unique identifier of the teacher", required = true)
            @PathVariable String teacherId) {
        List<ClassSection> classes = classService.getClassesByTeacher(teacherId);
        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a class section",
            description = "Updates an existing class section with new details. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Class section updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "404", description = "Class section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> updateClass(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated class section details", required = true)
            @RequestBody ClassSectionDTO dto) {
        ClassSection classSection = classService.updateClass(id, dto);
        return new ResponseEntity<>(classSection, HttpStatus.OK);
    }

    @PostMapping("/{classId}/students/{studentId}")
    @Operation(
            summary = "Add student to class",
            description = "Adds a student to a class section. Fails if the class has reached its maximum capacity or the student is already in the class."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student added to class successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "400", description = "Class full or student already in class"),
            @ApiResponse(responseCode = "404", description = "Class or student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> addStudentToClass(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId,
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        ClassSection classSection = classService.addStudentToClass(classId, studentId);
        return new ResponseEntity<>(classSection, HttpStatus.OK);
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    @Operation(
            summary = "Remove student from class",
            description = "Removes a student from a class section."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student removed from class successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "404", description = "Class or student not found in class"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> removeStudentFromClass(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId,
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        ClassSection classSection = classService.removeStudentFromClass(classId, studentId);
        return new ResponseEntity<>(classSection, HttpStatus.OK);
    }

    @PostMapping("/{classId}/courses/{courseId}")
    @Operation(
            summary = "Add course to class",
            description = "Assigns a course to a class section."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course added to class successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "404", description = "Class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> addCourseToClass(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId,
            @Parameter(description = "Unique identifier of the course", required = true)
            @PathVariable String courseId) {
        ClassSection classSection = classService.addCourseToClass(classId, courseId);
        return new ResponseEntity<>(classSection, HttpStatus.OK);
    }

    @DeleteMapping("/{classId}/courses/{courseId}")
    @Operation(
            summary = "Remove course from class",
            description = "Removes a course assignment from a class section."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course removed from class successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassSection.class))),
            @ApiResponse(responseCode = "404", description = "Class or course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassSection> removeCourseFromClass(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId,
            @Parameter(description = "Unique identifier of the course", required = true)
            @PathVariable String courseId) {
        ClassSection classSection = classService.removeCourseFromClass(classId, courseId);
        return new ResponseEntity<>(classSection, HttpStatus.OK);
    }

    @GetMapping("/{classId}/students")
    @Operation(
            summary = "Get class students",
            description = "Retrieves all students enrolled in a specific class section."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Student>> getClassStudents(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId) {
        List<Student> students = classService.getClassStudents(classId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/promote")
    @Operation(
            summary = "Promote a student",
            description = "Promotes a student from one class to another. Handles the transfer of the student between classes and records the promotion history."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student promoted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassPromotion.class))),
            @ApiResponse(responseCode = "400", description = "Invalid promotion data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassPromotion> promoteStudent(
            @Parameter(description = "Promotion details", required = true)
            @RequestBody ClassPromotionDTO dto) {
        ClassPromotion promotion = classService.promoteStudent(dto);
        return new ResponseEntity<>(promotion, HttpStatus.CREATED);
    }

    @PostMapping("/promote/bulk")
    @Operation(
            summary = "Bulk promote students",
            description = "Promotes multiple students from one class to another in a single operation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Students promoted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid promotion data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ClassPromotion>> bulkPromote(
            @Parameter(description = "Source class ID", required = true) @RequestParam String fromClassId,
            @Parameter(description = "Target class ID", required = true) @RequestParam String toClassId,
            @Parameter(description = "Target academic year", required = true) @RequestParam String toAcademicYear,
            @Parameter(description = "List of student IDs to promote", required = true) @RequestBody List<String> studentIds) {
        List<ClassPromotion> promotions = classService.bulkPromote(fromClassId, toClassId, toAcademicYear, studentIds);
        return new ResponseEntity<>(promotions, HttpStatus.CREATED);
    }

    @GetMapping("/promotions/student/{studentId}")
    @Operation(
            summary = "Get promotion history",
            description = "Retrieves the complete promotion history for a specific student."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promotion history retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ClassPromotion>> getPromotionHistory(
            @Parameter(description = "Unique identifier of the student", required = true)
            @PathVariable String studentId) {
        List<ClassPromotion> promotions = classService.getPromotionHistory(studentId);
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    @GetMapping("/{classId}/statistics")
    @Operation(
            summary = "Get class statistics",
            description = "Retrieves statistical information for a specific class section including capacity, current strength, available slots, and course count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Class statistics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassStatsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ClassStatsDTO> getClassStatistics(
            @Parameter(description = "Unique identifier of the class", required = true)
            @PathVariable String classId) {
        ClassStatsDTO stats = classService.getClassStatistics(classId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
