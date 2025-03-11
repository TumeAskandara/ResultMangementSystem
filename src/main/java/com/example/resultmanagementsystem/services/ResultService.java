package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.ResultDTO;
import com.example.resultmanagementsystem.model.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Result createResult(ResultDTO resultDTO) {
        // Extract values from DTO
        String studentId = resultDTO.getStudentId();
        String courseId = resultDTO.getCourseId();
        Double ca = resultDTO.getCa();
        Double exams = resultDTO.getExams();
        Double credits = resultDTO.getCredit();

        // Validate input scores
        if (ca < 0 || ca > 30) {
            throw new IllegalArgumentException("CA score must be between 0 and 30");
        }

        if (exams < 0 || exams > 70) {
            throw new IllegalArgumentException("Exam score must be between 0 and 70");
        }

        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be a positive number");
        }

        // Create new result object
        Result result = new Result();
        result.setStudentId(studentId);
        result.setCourseId(courseId);
        result.setCa(ca);
        result.setExams(exams);
        result.setCredits(credits);

        // Calculate total
        double total = ca + exams;
        result.setTotal(total);

        // Calculate grade, evaluation, and weight
        calculateGradeAndGPA(result, total);

        // Set status
        result.setStatus(total >= 50 ? "PASS" : "FAIL");

        // Set jury decision (default to normal decision based on scores)
        result.setJuryDecision(total >= 50 ? "VALIDATED" : "FAIL");

        // Save and return the result
        return resultRepository.save(result);
    }

    /**
     * Helper method to calculate grade, evaluation, weight, and GPA
     */
    private void calculateGradeAndGPA(Result result, double total) {
        String grade;
        String evaluation;
        double weight;

        if (total >= 90) {
            grade = "A+";
            evaluation = "Distinction";
            weight = 4.5;
        } else if (total >= 80) {
            grade = "A";
            evaluation = "Excellent";
            weight = 4.0;
        } else if (total >= 75) {
            grade = "B+";
            evaluation = "Very Good";
            weight = 3.5;
        } else if (total >= 70) {
            grade = "B";
            evaluation = "Good";
            weight = 3.0;
        } else if (total >= 65) {
            grade = "C+";
            evaluation = "Above Average";
            weight = 2.5;
        } else if (total >= 60) {
            grade = "C";
            evaluation = "Average";
            weight = 2.0;
        } else if (total >= 55) {
            grade = "D+";
            evaluation = "Below Average";
            weight = 1.5;
        } else if (total >= 50) {
            grade = "D";
            evaluation = "Pass";
            weight = 1.0;
        } else {
            grade = "F";
            evaluation = "Fail";
            weight = 0.0;
        }

        result.setGrade(grade);
        result.setEvaluation(evaluation);
        result.setWeight(weight);
    }

    /**
     * Retrieves all results for a specific student, and also calculates the cumulative GPA.
     */
    public Map<String, Object> getResultsByStudentId(String studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);

        // Remove gpa field from results before returning them
        for (Result result : results) {
            result.setGpa(null); // Set gpa to null, or don't include it in the response
        }

        // Calculate cumulative GPA for the student
        double cumulativeGPA = calculateCumulativeGPA(studentId);

        // Prepare the response to include both results and cumulative GPA
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("cumulativeGPA", cumulativeGPA);

        return response;
    }


    /**
     * Calculates the cumulative GPA for a student
     */
    public double calculateCumulativeGPA(String studentId) {
        List<Result> studentResults = resultRepository.findByStudentId(studentId);

        if (studentResults.isEmpty()) {
            return 0.0;
        }

        double totalWeightedPoints = 0.0;
        double totalCredits = 0.0;

        for (Result result : studentResults) {
            double creditValue = result.getCredits();
            totalWeightedPoints += result.getWeight() * creditValue;
            totalCredits += creditValue;
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        return totalWeightedPoints / totalCredits;
    }

    /**
     * Retrieves a result by its ID
     */
    public Result getResultById(String id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + id));
    }

    /**
     * Retrieves all results for a specific course
     */
    public List<Result> getResultsByCourseId(String courseId) {
        return resultRepository.findByCourseId(courseId);
    }

    /**
     * Updates an existing result
     */
    public Result updateResult(String id, double ca, double exams) {
        Result existingResult = getResultById(id);

        // Update scores
        existingResult.setCa(ca);
        existingResult.setExams(exams);

        // Recalculate total
        double total = ca + exams;
        existingResult.setTotal(total);

        // Recalculate grade, evaluation, weight
        calculateGradeAndGPA(existingResult, total);

        // Update status
        existingResult.setStatus(total >= 50 ? "PASS" : "FAIL");

        // Save and return the updated result
        return resultRepository.save(existingResult);
    }

    /**
     * Deletes a result by its ID
     */
    public void deleteResult(String id) {
        resultRepository.deleteById(id);
    }
}
