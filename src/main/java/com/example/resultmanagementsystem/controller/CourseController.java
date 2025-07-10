package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.model.Course;
import com.example.resultmanagementsystem.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Course Management", description = "Operations related to courses")
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Get all courses")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllCourses() {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Course> courses = courseService.getAllCourses();

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Courses retrieved successfully")
                            .build())
                    .data(courses)
                    .paginationDto(PaginationDTO.builder()
                            .count(courses.size())
                            .total(courses.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving all courses: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("Failed to retrieve courses")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a course by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getCourseById(@PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Optional<Course> course = courseService.getCourseById(id);

            if (course.isPresent()) {
                ResponseDTO response = ResponseDTO.builder()
                        .meta(MetaDTO.builder()
                                .statusCode(HttpStatus.OK.value())
                                .statusDescription("OK")
                                .message("Course retrieved successfully")
                                .build())
                        .data(course.get())
                        .correlationId(correlationId)
                        .transactionId(transactionId)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseDTO response = ResponseDTO.builder()
                        .meta(MetaDTO.builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .statusDescription("NOT_FOUND")
                                .message("Course not found")
                                .build())
                        .correlationId(correlationId)
                        .transactionId(transactionId)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error retrieving course {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("Failed to retrieve course")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all courses by department ID")
    @GetMapping("/getAllCoursesBydepartmentId/{departmentId}")
    public ResponseEntity<ResponseDTO> getAllCoursesByDepartmentId(@PathVariable String departmentId) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            List<Course> courses = courseService.getCoursesByDepartmentId(departmentId);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.OK.value())
                            .statusDescription("OK")
                            .message("Courses retrieved successfully by department")
                            .build())
                    .data(courses)
                    .paginationDto(PaginationDTO.builder()
                            .count(courses.size())
                            .total(courses.size())
                            .build())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error retrieving courses for department {}: {}", departmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("Failed to retrieve courses for department")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error retrieving courses for department {}: {}", departmentId, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusDescription("INTERNAL_SERVER_ERROR")
                            .message("An unexpected error occurred")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a new course")
    @PostMapping("/createCourse")
    public ResponseEntity<ResponseDTO> createCourse(@RequestBody Course course) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Course createdCourse = courseService.createCourse(course);

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusDescription("CREATED")
                            .message("Course created successfully")
                            .build())
                    .data(createdCourse)
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error creating course: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.CONFLICT.value())
                            .statusDescription("CONFLICT")
                            .message("Failed to create course")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Unexpected error creating course: {}", e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to create course")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing course")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateCourse(@PathVariable String id, @RequestBody Course course) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            Course updatedCourse = courseService.updateCourse(id, course);

            if (updatedCourse != null) {
                ResponseDTO response = ResponseDTO.builder()
                        .meta(MetaDTO.builder()
                                .statusCode(HttpStatus.OK.value())
                                .statusDescription("OK")
                                .message("Course updated successfully")
                                .build())
                        .data(updatedCourse)
                        .correlationId(correlationId)
                        .transactionId(transactionId)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseDTO response = ResponseDTO.builder()
                        .meta(MetaDTO.builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .statusDescription("NOT_FOUND")
                                .message("Course not found for update")
                                .build())
                        .correlationId(correlationId)
                        .transactionId(transactionId)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error updating course {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to update course")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a course")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCourse(@PathVariable String id) {
        String correlationId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        try {
            boolean deleted = courseService.deleteCourse(id);

            if (deleted) {
                ResponseDTO response = ResponseDTO.builder()
                        .meta(MetaDTO.builder()
                                .statusCode(HttpStatus.NO_CONTENT.value())
                                .statusDescription("NO_CONTENT")
                                .message("Course deleted successfully")
                                .build())
                        .correlationId(correlationId)
                        .transactionId(transactionId)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                ResponseDTO response = ResponseDTO.builder()
                        .meta(MetaDTO.builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .statusDescription("NOT_FOUND")
                                .message("Course not found for deletion")
                                .build())
                        .correlationId(correlationId)
                        .transactionId(transactionId)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting course {}: {}", id, e.getMessage());

            ResponseDTO response = ResponseDTO.builder()
                    .meta(MetaDTO.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusDescription("BAD_REQUEST")
                            .message("Failed to delete course")
                            .build())
                    .error(e.getMessage())
                    .correlationId(correlationId)
                    .transactionId(transactionId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}