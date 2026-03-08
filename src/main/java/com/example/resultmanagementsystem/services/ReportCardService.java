package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.ReportCardRequestDTO;
import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.DepartmentRepository;
import com.example.resultmanagementsystem.Dto.Repository.ReportCardRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.Dto.Repository.TranscriptRepository;
import com.example.resultmanagementsystem.Dto.TranscriptRequestDTO;
import com.example.resultmanagementsystem.model.Course;
import com.example.resultmanagementsystem.model.Department;
import com.example.resultmanagementsystem.model.ReportCard;
import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.model.Transcript;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportCardService {

    private final ReportCardRepository reportCardRepository;
    private final TranscriptRepository transcriptRepository;
    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public ReportCard generateReportCard(ReportCardRequestDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + dto.getStudentId()));

        Department department = null;
        if (student.getDepartmentId() != null) {
            department = departmentRepository.findByDepartmentId(student.getDepartmentId());
        }

        List<Result> results = resultRepository.findByStudentIdAndSemesterAndYear(
                dto.getStudentId(), dto.getSemester(), dto.getAcademicYear());

        if (results.isEmpty()) {
            throw new RuntimeException("No results found for student " + dto.getStudentId()
                    + " in semester " + dto.getSemester() + " year " + dto.getAcademicYear());
        }

        List<ReportCard.ReportCardEntry> entries = new ArrayList<>();
        double totalWeightedGPA = 0;
        double totalCredits = 0;
        double totalCreditsEarned = 0;

        for (Result result : results) {
            Course course = courseRepository.findById(result.getCourseId()).orElse(null);

            ReportCard.ReportCardEntry entry = new ReportCard.ReportCardEntry();
            entry.setCourseCode(course != null ? course.getCode() : result.getCourseId());
            entry.setCourseTitle(course != null ? course.getCourseTitle() : "Unknown");
            entry.setCredits(result.getCredits());
            entry.setCa(result.getCa());
            entry.setExams(result.getExams());
            entry.setTotal(result.getTotal());
            entry.setGrade(result.getGrade());
            entry.setGradePoint(result.getGpa());
            entry.setStatus(result.getStatus());
            entries.add(entry);

            totalCredits += result.getCredits();
            totalWeightedGPA += result.getGpa() * result.getCredits();
            if ("PASS".equalsIgnoreCase(result.getStatus()) || "VALIDATED".equalsIgnoreCase(result.getStatus())) {
                totalCreditsEarned += result.getCredits();
            }
        }

        double semesterGPA = totalCredits > 0 ? roundToTwoDecimals(totalWeightedGPA / totalCredits) : 0;

        // Calculate cumulative GPA across all semesters
        List<Result> allResults = resultRepository.findByStudentId(dto.getStudentId());
        double cumulativeWeighted = 0;
        double cumulativeCredits = 0;
        for (Result r : allResults) {
            cumulativeWeighted += r.getGpa() * r.getCredits();
            cumulativeCredits += r.getCredits();
        }
        double cumulativeGPA = cumulativeCredits > 0 ? roundToTwoDecimals(cumulativeWeighted / cumulativeCredits) : 0;

        // Calculate class rank
        List<Student> deptStudents = studentRepository.findByDepartmentId(student.getDepartmentId());
        List<Double> gpas = new ArrayList<>();
        for (Student s : deptStudents) {
            List<Result> sResults = resultRepository.findByStudentIdAndSemesterAndYear(
                    s.getStudentId(), dto.getSemester(), dto.getAcademicYear());
            if (!sResults.isEmpty()) {
                double wGpa = 0;
                double wCredits = 0;
                for (Result r : sResults) {
                    wGpa += r.getGpa() * r.getCredits();
                    wCredits += r.getCredits();
                }
                gpas.add(wCredits > 0 ? wGpa / wCredits : 0);
            }
        }
        gpas.sort(Comparator.reverseOrder());
        int rank = gpas.indexOf(semesterGPA) + 1;
        if (rank == 0) rank = gpas.size();

        ReportCard reportCard = new ReportCard();
        reportCard.setId(UUID.randomUUID().toString());
        reportCard.setStudentId(student.getStudentId());
        reportCard.setStudentName(student.getName());
        reportCard.setDepartmentId(student.getDepartmentId());
        reportCard.setDepartmentName(department != null ? department.getDepartmentName() : "Unknown");
        reportCard.setAcademicYear(dto.getAcademicYear());
        reportCard.setSemester(dto.getSemester());
        reportCard.setResults(entries);
        reportCard.setSemesterGPA(semesterGPA);
        reportCard.setCumulativeGPA(cumulativeGPA);
        reportCard.setTotalCredits(totalCredits);
        reportCard.setTotalCreditsEarned(totalCreditsEarned);
        reportCard.setClassRank(rank);
        reportCard.setTotalStudentsInClass(gpas.size());
        reportCard.setRemarks(generateRemarks(semesterGPA));
        reportCard.setGeneratedAt(LocalDateTime.now());
        reportCard.setStatus(ReportCard.ReportCardStatus.DRAFT);

        return reportCardRepository.save(reportCard);
    }

    public ReportCard getReportCard(String id) {
        return reportCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report card not found with ID: " + id));
    }

    public List<ReportCard> getReportCardsByStudent(String studentId) {
        return reportCardRepository.findByStudentId(studentId);
    }

    public List<ReportCard> getReportCardsByDepartment(String departmentId, String academicYear, String semester) {
        return reportCardRepository.findByDepartmentIdAndAcademicYearAndSemester(departmentId, academicYear, semester);
    }

    public ReportCard publishReportCard(String id) {
        ReportCard reportCard = getReportCard(id);
        reportCard.setStatus(ReportCard.ReportCardStatus.PUBLISHED);
        return reportCardRepository.save(reportCard);
    }

    public List<ReportCard> generateBulkReportCards(String departmentId, String academicYear, String semester) {
        List<Student> students = studentRepository.findByDepartmentId(departmentId);
        List<ReportCard> reportCards = new ArrayList<>();

        for (Student student : students) {
            List<Result> results = resultRepository.findByStudentIdAndSemesterAndYear(
                    student.getStudentId(), semester, academicYear);
            if (!results.isEmpty()) {
                try {
                    ReportCardRequestDTO request = new ReportCardRequestDTO();
                    request.setStudentId(student.getStudentId());
                    request.setAcademicYear(academicYear);
                    request.setSemester(semester);
                    ReportCard card = generateReportCard(request);
                    reportCards.add(card);
                } catch (Exception e) {
                    // Skip students with errors and continue
                }
            }
        }

        return reportCards;
    }

    public Transcript generateTranscript(TranscriptRequestDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + dto.getStudentId()));

        Department department = null;
        if (student.getDepartmentId() != null) {
            department = departmentRepository.findByDepartmentId(student.getDepartmentId());
        }

        List<Result> allResults = resultRepository.findByStudentId(dto.getStudentId());

        if (allResults.isEmpty()) {
            throw new RuntimeException("No results found for student " + dto.getStudentId());
        }

        // Group results by year and semester
        Map<String, List<Result>> grouped = allResults.stream()
                .collect(Collectors.groupingBy(r -> r.getYear() + "|" + r.getSemester()));

        List<Transcript.TranscriptSemester> semesters = new ArrayList<>();
        double totalCumulativeWeighted = 0;
        double totalCreditsAttempted = 0;
        double totalCreditsEarned = 0;

        for (Map.Entry<String, List<Result>> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            String year = parts[0];
            String sem = parts.length > 1 ? parts[1] : "";
            List<Result> semResults = entry.getValue();

            List<Transcript.TranscriptCourse> courses = new ArrayList<>();
            double semWeighted = 0;
            double semCreditsAttempted = 0;
            double semCreditsEarned = 0;

            for (Result r : semResults) {
                Course course = courseRepository.findById(r.getCourseId()).orElse(null);
                Transcript.TranscriptCourse tc = new Transcript.TranscriptCourse();
                tc.setCourseCode(course != null ? course.getCode() : r.getCourseId());
                tc.setCourseTitle(course != null ? course.getCourseTitle() : "Unknown");
                tc.setCredits(r.getCredits());
                tc.setGrade(r.getGrade());
                tc.setGradePoint(r.getGpa());
                courses.add(tc);

                semCreditsAttempted += r.getCredits();
                semWeighted += r.getGpa() * r.getCredits();
                if ("PASS".equalsIgnoreCase(r.getStatus()) || "VALIDATED".equalsIgnoreCase(r.getStatus())) {
                    semCreditsEarned += r.getCredits();
                }
            }

            double semGPA = semCreditsAttempted > 0 ? roundToTwoDecimals(semWeighted / semCreditsAttempted) : 0;

            Transcript.TranscriptSemester ts = new Transcript.TranscriptSemester();
            ts.setAcademicYear(year);
            ts.setSemester(sem);
            ts.setCourses(courses);
            ts.setSemesterGPA(semGPA);
            ts.setCreditsAttempted(semCreditsAttempted);
            ts.setCreditsEarned(semCreditsEarned);
            semesters.add(ts);

            totalCumulativeWeighted += semWeighted;
            totalCreditsAttempted += semCreditsAttempted;
            totalCreditsEarned += semCreditsEarned;
        }

        double cumulativeGPA = totalCreditsAttempted > 0 ? roundToTwoDecimals(totalCumulativeWeighted / totalCreditsAttempted) : 0;

        Transcript transcript = new Transcript();
        transcript.setId(UUID.randomUUID().toString());
        transcript.setStudentId(student.getStudentId());
        transcript.setStudentName(student.getName());
        transcript.setRegistrationNumber(student.getRegistrationNumber());
        transcript.setDepartmentId(student.getDepartmentId());
        transcript.setDepartmentName(department != null ? department.getDepartmentName() : "Unknown");
        transcript.setSemesters(semesters);
        transcript.setCumulativeGPA(cumulativeGPA);
        transcript.setTotalCreditsAttempted(totalCreditsAttempted);
        transcript.setTotalCreditsEarned(totalCreditsEarned);
        transcript.setGraduationStatus(Transcript.GraduationStatus.IN_PROGRESS);
        transcript.setGeneratedAt(LocalDateTime.now());
        transcript.setVerificationCode(UUID.randomUUID().toString());

        return transcriptRepository.save(transcript);
    }

    public Transcript getTranscript(String id) {
        return transcriptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transcript not found with ID: " + id));
    }

    public Transcript verifyTranscript(String verificationCode) {
        return transcriptRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new RuntimeException("Transcript not found with verification code: " + verificationCode));
    }

    // ===== Helper Methods =====

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private String generateRemarks(double gpa) {
        if (gpa >= 3.7) return "Excellent performance. Dean's list candidate.";
        if (gpa >= 3.0) return "Very good performance. Keep up the good work.";
        if (gpa >= 2.5) return "Good performance. Room for improvement.";
        if (gpa >= 2.0) return "Satisfactory performance. More effort required.";
        if (gpa >= 1.0) return "Below average. Academic probation may apply.";
        return "Poor performance. Academic intervention required.";
    }
}
