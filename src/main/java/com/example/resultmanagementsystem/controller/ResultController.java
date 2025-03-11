package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ResultDTO;
import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins ="*", maxAge = 3600)
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }


    @PostMapping
    public ResponseEntity<Result> createResult(@RequestBody ResultDTO resultDTO) {
        try {
            String studentId = resultDTO.getStudentId();
            String courseId = resultDTO.getCourseId();
            double ca = resultDTO.getCa();
            double exams = resultDTO.getExams();
            double credits = resultDTO.getCredit(); // Note: This matches the DTO field name

            Result createdResult = resultService.createResult(resultDTO);
            return new ResponseEntity<>(createdResult, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create result: " + e.getMessage());
        }
    }

    @GetMapping("/getResultById{id}")
    public ResponseEntity<Result> getResultById(@PathVariable String id) {
        try {
            Result result = resultService.getResultById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     */
    @GetMapping("/student/getResultByStudentId{studentId}")
    public ResponseEntity<List<Result>> getResultsByStudentId(@PathVariable String studentId) {
        Map<String,Object> results = resultService.getResultsByStudentId(studentId);
        return new ResponseEntity(results, HttpStatus.OK);
    }


    @GetMapping("/course/getResultByCourseId{courseId}")
    public ResponseEntity<List<Result>> getResultsByCourseId(@PathVariable String courseId) {
        List<Result> results = resultService.getResultsByCourseId(courseId);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    /**
     * Calculate cumulative GPA for a student
     */
    @GetMapping("/student/{studentId}/gpa")
    public ResponseEntity<Map<String, Object>> calculateCumulativeGPA(@PathVariable String studentId) {
        double cumulativeGPA = resultService.calculateCumulativeGPA(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("cumulativeGPA", cumulativeGPA);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update a result
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {
        try {
            double ca = Double.parseDouble(request.get("ca").toString());
            double exams = Double.parseDouble(request.get("exams").toString());

            Result updatedResult = resultService.updateResult(id, ca, exams);
            return new ResponseEntity<>(updatedResult, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
     * Delete a result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteResult(@PathVariable String id) {
        try {
            resultService.deleteResult(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Result with ID " + id + " successfully deleted");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Exception handler for IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation Error");
        errorResponse.put("message", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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