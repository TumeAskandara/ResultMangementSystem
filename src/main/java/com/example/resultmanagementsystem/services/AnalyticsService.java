package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.Dto.Repository.*;
import com.example.resultmanagementsystem.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnalyticsService {

    private final AnalyticsReportRepository analyticsReportRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ResultRepository resultRepository;
    private final FeeRepository feeRepository;
    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final AdmissionApplicationRepository admissionApplicationRepository;
    private final ComplaintRepository complaintRepository;
    private final StaffRepository staffRepository;

    public StudentPerformanceAnalyticsDTO getStudentPerformanceAnalytics(String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        Department department = departmentRepository.findByDepartmentId(student.getDepartmentId());
        String departmentName = department != null ? department.getDepartmentName() : "Unknown";

        List<Result> results = resultRepository.findByStudentId(studentId);

        Map<String, Double> semesterGPAs = new LinkedHashMap<>();
        Map<String, List<Result>> resultsBySemester = results.stream()
                .collect(Collectors.groupingBy(r -> r.getYear() + "-" + r.getSemester()));

        for (Map.Entry<String, List<Result>> entry : resultsBySemester.entrySet()) {
            double avgGpa = entry.getValue().stream()
                    .mapToDouble(Result::getGpa)
                    .average()
                    .orElse(0.0);
            semesterGPAs.put(entry.getKey(), Math.round(avgGpa * 100.0) / 100.0);
        }

        double cumulativeGPA = results.stream()
                .mapToDouble(Result::getGpa)
                .average()
                .orElse(0.0);

        // Calculate attendance percentage
        List<Attendance> attendances = new ArrayList<>();
        List<Course> studentCourses = courseRepository.findCourseByDepartmentId(student.getDepartmentId());
        for (Course course : studentCourses) {
            attendances.addAll(attendanceRepository.findByStudentIdAndCourseId(studentId, course.getId()));
        }
        long totalAttendance = attendances.size();
        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT
                        || a.getStatus() == Attendance.AttendanceStatus.LATE)
                .count();
        double attendancePercentage = totalAttendance > 0 ? (presentCount * 100.0 / totalAttendance) : 0.0;

        // Determine trend
        List<Double> gpas = new ArrayList<>(semesterGPAs.values());
        StudentPerformanceAnalyticsDTO.PerformanceTrend trend = StudentPerformanceAnalyticsDTO.PerformanceTrend.STABLE;
        if (gpas.size() >= 2) {
            double last = gpas.get(gpas.size() - 1);
            double secondLast = gpas.get(gpas.size() - 2);
            if (last > secondLast + 0.1) {
                trend = StudentPerformanceAnalyticsDTO.PerformanceTrend.IMPROVING;
            } else if (last < secondLast - 0.1) {
                trend = StudentPerformanceAnalyticsDTO.PerformanceTrend.DECLINING;
            }
        }

        return StudentPerformanceAnalyticsDTO.builder()
                .studentId(studentId)
                .studentName(student.getName())
                .departmentName(departmentName)
                .semesterGPAs(semesterGPAs)
                .cumulativeGPA(Math.round(cumulativeGPA * 100.0) / 100.0)
                .attendancePercentage(Math.round(attendancePercentage * 100.0) / 100.0)
                .assignmentsCompleted(0)
                .totalAssignments(0)
                .trend(trend)
                .build();
    }

    public DepartmentAnalyticsDTO getDepartmentAnalytics(String departmentId, String academicYear) {
        Department department = departmentRepository.findByDepartmentId(departmentId);
        if (department == null) {
            throw new RuntimeException("Department not found with ID: " + departmentId);
        }

        List<Student> students = studentRepository.findByDepartmentId(departmentId);
        long totalStudents = students.size();

        double averageGPA = 0.0;
        double passRate = 0.0;
        List<String> topPerformers = new ArrayList<>();

        if (!students.isEmpty()) {
            Map<String, Double> studentGPAs = new HashMap<>();
            int passCount = 0;
            int totalWithResults = 0;

            for (Student s : students) {
                List<Result> results = resultRepository.findByStudentId(s.getStudentId());
                if (!results.isEmpty()) {
                    double avgGpa = results.stream().mapToDouble(Result::getGpa).average().orElse(0.0);
                    studentGPAs.put(s.getName(), avgGpa);
                    totalWithResults++;
                    boolean passed = results.stream().allMatch(r -> !"FAIL".equalsIgnoreCase(r.getStatus()));
                    if (passed) passCount++;
                }
            }

            averageGPA = studentGPAs.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            passRate = totalWithResults > 0 ? (passCount * 100.0 / totalWithResults) : 0.0;

            topPerformers = studentGPAs.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        List<Enrollment> enrollments = enrollmentRepository.findByDepartmentId(departmentId);
        long enrollmentCount = enrollments.stream()
                .filter(e -> academicYear == null || academicYear.equals(e.getAcademicYear()))
                .count();

        return DepartmentAnalyticsDTO.builder()
                .departmentId(departmentId)
                .departmentName(department.getDepartmentName())
                .totalStudents(totalStudents)
                .averageGPA(Math.round(averageGPA * 100.0) / 100.0)
                .attendanceRate(0.0)
                .passRate(Math.round(passRate * 100.0) / 100.0)
                .topPerformers(topPerformers)
                .enrollmentCount(enrollmentCount)
                .build();
    }

    public FeeAnalyticsDTO getFeeAnalytics(String academicYear) {
        List<Fee> fees = feeRepository.findAll();
        if (academicYear != null && !academicYear.isEmpty()) {
            fees = fees.stream()
                    .filter(f -> academicYear.equals(f.getAcademicYear()))
                    .collect(Collectors.toList());
        }

        double totalExpected = fees.stream()
                .mapToDouble(f -> f.getTotalAmount() != null ? f.getTotalAmount() : 0.0)
                .sum();
        double totalCollected = fees.stream()
                .mapToDouble(f -> f.getPaidAmount() != null ? f.getPaidAmount() : 0.0)
                .sum();
        double totalOutstanding = totalExpected - totalCollected;
        double collectionRate = totalExpected > 0 ? (totalCollected * 100.0 / totalExpected) : 0.0;

        Map<String, Double> departmentBreakdown = new HashMap<>();
        List<Department> allDepartments = departmentRepository.findAll();
        for (Department dept : allDepartments) {
            List<Student> deptStudents = studentRepository.findByDepartmentId(dept.getDepartmentId());
            double deptCollected = 0.0;
            for (Student s : deptStudents) {
                List<Fee> studentFees = feeRepository.findByStudentId(s.getStudentId());
                if (academicYear != null) {
                    studentFees = studentFees.stream()
                            .filter(f -> academicYear.equals(f.getAcademicYear()))
                            .collect(Collectors.toList());
                }
                deptCollected += studentFees.stream()
                        .mapToDouble(f -> f.getPaidAmount() != null ? f.getPaidAmount() : 0.0)
                        .sum();
            }
            if (deptCollected > 0) {
                departmentBreakdown.put(dept.getDepartmentName(), deptCollected);
            }
        }

        return FeeAnalyticsDTO.builder()
                .totalExpected(totalExpected)
                .totalCollected(totalCollected)
                .totalOutstanding(totalOutstanding)
                .collectionRate(Math.round(collectionRate * 100.0) / 100.0)
                .departmentBreakdown(departmentBreakdown)
                .build();
    }

    public Map<String, Object> getAttendanceTrend(String departmentId, String academicYear, String semester) {
        Map<String, Object> trend = new LinkedHashMap<>();
        trend.put("departmentId", departmentId);
        trend.put("academicYear", academicYear);
        trend.put("semester", semester);

        List<Student> students = studentRepository.findByDepartmentId(departmentId);
        List<Map<String, Object>> monthlyData = new ArrayList<>();

        trend.put("totalStudents", students.size());
        trend.put("monthlyData", monthlyData);

        return trend;
    }

    public Map<String, Object> getEnrollmentTrend(String academicYear) {
        Map<String, Object> trend = new LinkedHashMap<>();
        List<Enrollment> enrollments = enrollmentRepository.findByAcademicYear(academicYear);

        trend.put("academicYear", academicYear);
        trend.put("totalEnrollments", enrollments.size());

        Map<String, Long> byStatus = enrollments.stream()
                .collect(Collectors.groupingBy(e -> e.getStatus() != null ? e.getStatus().name() : "UNKNOWN", Collectors.counting()));
        trend.put("byStatus", byStatus);

        Map<String, Long> byDepartment = new HashMap<>();
        for (Enrollment e : enrollments) {
            if (e.getDepartmentId() != null) {
                Department dept = departmentRepository.findByDepartmentId(e.getDepartmentId());
                String name = dept != null ? dept.getDepartmentName() : e.getDepartmentId();
                byDepartment.merge(name, 1L, Long::sum);
            }
        }
        trend.put("byDepartment", byDepartment);

        return trend;
    }

    public Map<String, Object> getExamAnalysis(String examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));

        List<ExamResult> results = examResultRepository.findByExamId(examId);

        Map<String, Object> analysis = new LinkedHashMap<>();
        analysis.put("examId", examId);
        analysis.put("examTitle", exam.getTitle());
        analysis.put("totalMarks", exam.getTotalMarks());
        analysis.put("passingMarks", exam.getPassingMarks());
        analysis.put("totalStudents", results.size());

        if (!results.isEmpty()) {
            double avgScore = results.stream().mapToDouble(ExamResult::getTotalScore).average().orElse(0.0);
            double maxScore = results.stream().mapToDouble(ExamResult::getTotalScore).max().orElse(0.0);
            double minScore = results.stream().mapToDouble(ExamResult::getTotalScore).min().orElse(0.0);
            long passedCount = results.stream()
                    .filter(r -> r.getStatus() == ExamResult.ExamResultStatus.PASSED)
                    .count();
            long failedCount = results.stream()
                    .filter(r -> r.getStatus() == ExamResult.ExamResultStatus.FAILED)
                    .count();

            analysis.put("averageScore", Math.round(avgScore * 100.0) / 100.0);
            analysis.put("highestScore", maxScore);
            analysis.put("lowestScore", minScore);
            analysis.put("passedCount", passedCount);
            analysis.put("failedCount", failedCount);
            analysis.put("passRate", Math.round(passedCount * 100.0 / results.size() * 100.0) / 100.0);

            Map<String, Long> gradeDistribution = results.stream()
                    .filter(r -> r.getGrade() != null)
                    .collect(Collectors.groupingBy(ExamResult::getGrade, Collectors.counting()));
            analysis.put("gradeDistribution", gradeDistribution);
        }

        return analysis;
    }

    public DashboardStatsDTO getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalCourses = courseRepository.count();
        long totalDepartments = departmentRepository.count();
        long totalStaff = staffRepository.count();

        long activeEnrollments = enrollmentRepository.findByStatus(Enrollment.EnrollmentStatus.ACTIVE).size();
        long pendingAdmissions = admissionApplicationRepository.countByStatus(
                AdmissionApplication.ApplicationStatus.PENDING);
        long pendingComplaints = complaintRepository.findByStatus(Complaint.ComplaintStatus.PENDING).size();

        // Fee collection rate
        List<Fee> allFees = feeRepository.findAll();
        double totalExpected = allFees.stream()
                .mapToDouble(f -> f.getTotalAmount() != null ? f.getTotalAmount() : 0.0).sum();
        double totalCollected = allFees.stream()
                .mapToDouble(f -> f.getPaidAmount() != null ? f.getPaidAmount() : 0.0).sum();
        double feeCollectionRate = totalExpected > 0 ? (totalCollected * 100.0 / totalExpected) : 0.0;

        // Overall pass rate
        List<Result> allResults = resultRepository.findAll();
        long passedResults = allResults.stream()
                .filter(r -> !"FAIL".equalsIgnoreCase(r.getStatus()))
                .count();
        double overallPassRate = allResults.isEmpty() ? 0.0 : (passedResults * 100.0 / allResults.size());

        return DashboardStatsDTO.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalCourses(totalCourses)
                .totalDepartments(totalDepartments)
                .totalStaff(totalStaff)
                .activeEnrollments(activeEnrollments)
                .pendingAdmissions(pendingAdmissions)
                .pendingComplaints(pendingComplaints)
                .pendingLeaves(0)
                .overdueBooks(0)
                .pendingMaintenance(0)
                .feeCollectionRate(Math.round(feeCollectionRate * 100.0) / 100.0)
                .averageAttendance(0.0)
                .overallPassRate(Math.round(overallPassRate * 100.0) / 100.0)
                .build();
    }

    public AnalyticsReport generateReport(AnalyticsRequestDTO request) {
        Map<String, Object> data = new LinkedHashMap<>();
        String title = "";

        switch (request.getReportType()) {
            case STUDENT_PERFORMANCE:
                if (request.getStudentId() != null) {
                    data.put("performance", getStudentPerformanceAnalytics(request.getStudentId()));
                    title = "Student Performance Report";
                }
                break;
            case ATTENDANCE_TREND:
                data.put("attendanceTrend", getAttendanceTrend(request.getDepartmentId(),
                        request.getAcademicYear(), request.getSemester()));
                title = "Attendance Trend Report";
                break;
            case FEE_COLLECTION:
                data.put("feeAnalytics", getFeeAnalytics(request.getAcademicYear()));
                title = "Fee Collection Report";
                break;
            case ENROLLMENT_TREND:
                data.put("enrollmentTrend", getEnrollmentTrend(request.getAcademicYear()));
                title = "Enrollment Trend Report";
                break;
            case DEPARTMENT_COMPARISON:
                if (request.getDepartmentId() != null) {
                    data.put("departmentAnalytics", getDepartmentAnalytics(request.getDepartmentId(),
                            request.getAcademicYear()));
                    title = "Department Comparison Report";
                }
                break;
            case EXAM_ANALYSIS:
                if (request.getCourseId() != null) {
                    data.put("examAnalysis", getExamAnalysis(request.getCourseId()));
                    title = "Exam Analysis Report";
                }
                break;
            default:
                title = "General Report";
                break;
        }

        Map<String, String> parameters = new LinkedHashMap<>();
        if (request.getAcademicYear() != null) parameters.put("academicYear", request.getAcademicYear());
        if (request.getSemester() != null) parameters.put("semester", request.getSemester());
        if (request.getDepartmentId() != null) parameters.put("departmentId", request.getDepartmentId());
        if (request.getStudentId() != null) parameters.put("studentId", request.getStudentId());
        if (request.getCourseId() != null) parameters.put("courseId", request.getCourseId());

        AnalyticsReport report = AnalyticsReport.builder()
                .id(UUID.randomUUID().toString())
                .reportType(request.getReportType())
                .title(title)
                .parameters(parameters)
                .data(data)
                .generatedBy("system")
                .generatedAt(LocalDateTime.now())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .build();

        return analyticsReportRepository.save(report);
    }

    public AnalyticsReport getReportById(String id) {
        return analyticsReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
    }

    public Page<AnalyticsReport> getReportsByType(String reportType, Pageable pageable) {
        AnalyticsReport.ReportType type = AnalyticsReport.ReportType.valueOf(reportType.toUpperCase());
        return analyticsReportRepository.findByReportType(type, pageable);
    }
}
