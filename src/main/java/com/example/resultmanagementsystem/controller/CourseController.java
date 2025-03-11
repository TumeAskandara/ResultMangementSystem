package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.model.Course;

import com.example.resultmanagementsystem.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Course Management", description = "Operations related to courses")
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Get all courses")
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Get a course by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new course")
    @PostMapping("/createCourse")
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return ResponseEntity.ok(createdCourse);
        } catch (RuntimeException e) {
            // Create an error response with appropriate status code
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
    @Operation(summary = "Update an existing course")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable String id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return updatedCourse != null ? ResponseEntity.ok(updatedCourse) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a course")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
