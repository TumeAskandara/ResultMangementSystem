package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.Dto.ResultDTO;
import com.example.resultmanagementsystem.model.Course;
import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public Result createResult(ResultDTO resultDTO) {
        String studentId = resultDTO.getStudentId();
        String courseId = resultDTO.getCourseId();
        String semester = resultDTO.getSemester();
        Double ca = resultDTO.getCa();
        Double exams = resultDTO.getExams();
        Double credits = resultDTO.getCredit();

        // Validate input values
        if (ca < 0 || ca > 30) throw new IllegalArgumentException("CA must be between 0 and 30");
        if (exams < 0 || exams > 70) throw new IllegalArgumentException("Exam must be between 0 and 70");
        if (credits <= 0) throw new IllegalArgumentException("Credits must be a positive number");

        // Create and populate Result object
        Result result = new Result();
        result.setStudentId(studentId);
        result.setCourseId(courseId);
        result.setSemester(semester);
        result.setCa(ca);
        result.setExams(exams);
        result.setCredits(credits);

        // Compute total, grade, GPA, and status
        double total = ca + exams;
        result.setTotal(total);
        calculateGradeAndGPA(result, total);
        result.setStatus(total >= 50 ? "PASS" : "FAIL");
        result.setJuryDecision(total >= 50 ? "VALIDATED" : "FAIL");

        return resultRepository.save(result);
    }

    private void calculateGradeAndGPA(Result result, double total) {
        String grade, evaluation;
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
        result.setGpa((int) weight);  // GPA should not be null, set it here.
    }

    public List<Result> getAllResultByDepartmentId(String id) {
        return resultRepository.findByStudentId(id);
    }

    // Service
    public Map<String, Object> getResultsByStudentEmailAndSemester(String email, String semester, String year) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student with email " + email + " not found");
        }

        String studentId = studentOpt.get().getStudentId();
        List<Result> results = resultRepository.findByStudentIdAndSemesterAndYear(studentId, semester, year);

        if (results.isEmpty()) {
            throw new RuntimeException("No results found for student with email " + email + " in semester " + semester);
        }

        return formatResults(results);
    }

    public Map<String, Object> getResultsByStudentEmail(String email) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student with email " + email + " not found");
        }

        return getResultsByStudentId(studentOpt.get().getStudentId());
    }

    public List<Result> getResultsBySemester(String studentId, String semester, String year) {
        return resultRepository.findByStudentIdAndSemesterAndYear(studentId, semester, year);
    }

    public Map<String, Object> getResultsByStudentId(String studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        return formatResults(results);
    }

    private Map<String, Object> formatResults(List<Result> results) {
        List<Map<String, Object>> formattedResults = new ArrayList<>();

        for (Result result : results) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("status", result.getStatus());
            resultMap.put("exams", result.getExams());
            resultMap.put("ca", result.getCa());
            resultMap.put("total", result.getTotal());
            resultMap.put("grade", result.getGrade());
            resultMap.put("evaluation", result.getEvaluation());
            resultMap.put("weight", result.getWeight());
            resultMap.put("juryDecision", result.getJuryDecision());
            resultMap.put("gpa", result.getGpa());  // Ensure GPA is included

            courseRepository.findById(result.getCourseId()).ifPresentOrElse(course -> {
                resultMap.put("courseTitle", course.getCourseTitle());
                resultMap.put("courseMaster", course.getCourseMaster());
                resultMap.put("credits", course.getCredits());
                resultMap.put("code", course.getCode());
            }, () -> {
                resultMap.put("courseTitle", "Unknown");
                resultMap.put("courseMaster", "Unknown");
            });

            formattedResults.add(resultMap);
        }

        double cumulativeGPA = calculateGPABySemester(results.get(0).getStudentId(), null, null);

        Map<String, Object> response = new HashMap<>();
        response.put("results", formattedResults);
        response.put("cumulativeGPA", cumulativeGPA);

        return response;
    }

    public double calculateGPABySemester(String studentId, String semester, String year) {
        List<Result> results = (semester == null) ?
                resultRepository.findByStudentId(studentId) :
                resultRepository.findByStudentIdAndSemesterAndYear(studentId, semester, year);

        if (results.isEmpty()) return 0.0;

        double totalWeightedPoints = results.stream().mapToDouble(r -> r.getWeight() * r.getCredits()).sum();
        double totalCredits = results.stream().mapToDouble(Result::getCredits).sum();

        return totalCredits == 0 ? 0.0 : totalWeightedPoints / totalCredits;
    }

    public Result getResultById(String id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + id));
    }

    public List<Result> getResultsByCourseId(String courseId) {
        return resultRepository.findByCourseId(courseId);
    }

    public Result updateResult(String id, double ca, double exams) {
        Result existingResult = getResultById(id);

        existingResult.setCa(ca);
        existingResult.setExams(exams);
        double total = ca + exams;
        existingResult.setTotal(total);
        calculateGradeAndGPA(existingResult, total);
        existingResult.setStatus(total >= 50 ? "PASS" : "FAIL");

        return resultRepository.save(existingResult);
    }

    public void deleteResult(String id) {
        resultRepository.deleteById(id);
    }
}
