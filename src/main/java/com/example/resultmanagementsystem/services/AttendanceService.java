package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.AttendanceDTO;
import com.example.resultmanagementsystem.Dto.AttendanceReportDTO;
import com.example.resultmanagementsystem.Dto.BulkAttendanceDTO;
import com.example.resultmanagementsystem.Dto.Repository.AttendanceRepository;
import com.example.resultmanagementsystem.Dto.Repository.AttendanceSummaryRepository;
import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.Attendance;
import com.example.resultmanagementsystem.model.AttendanceSummary;
import com.example.resultmanagementsystem.model.Course;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceSummaryRepository attendanceSummaryRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public Attendance markAttendance(AttendanceDTO dto, String markedBy) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(dto.getStudentId());
        attendance.setClassId(dto.getClassId());
        attendance.setCourseId(dto.getCourseId());
        attendance.setDate(dto.getDate());
        attendance.setStatus(dto.getStatus());
        attendance.setRemarks(dto.getRemarks());
        attendance.setSessionType(dto.getSessionType());
        attendance.setMarkedBy(markedBy);
        attendance.setMarkedAt(LocalDateTime.now());

        // Populate departmentId and teacherId from student and course
        Student student = studentRepository.findByStudentId(dto.getStudentId());
        if (student != null) {
            attendance.setDepartmentId(student.getDepartmentId());
        }

        Optional<Course> courseOpt = courseRepository.findById(dto.getCourseId());
        if (courseOpt.isPresent()) {
            attendance.setTeacherId(courseOpt.get().getTeacherId());
            attendance.setAcademicYear(dto.getDate().getYear() + "");
            attendance.setSemester(courseOpt.get().getSemester());
        }

        Attendance saved = attendanceRepository.save(attendance);

        // Update summary after marking
        updateAttendanceSummary(dto.getStudentId(), dto.getCourseId());

        return saved;
    }

    public List<Attendance> markBulkAttendance(BulkAttendanceDTO dto, String markedBy) {
        List<Attendance> attendanceList = new ArrayList<>();

        for (var record : dto.getRecords()) {
            AttendanceDTO attendanceDTO = new AttendanceDTO();
            attendanceDTO.setStudentId(record.getStudentId());
            attendanceDTO.setClassId(dto.getClassId());
            attendanceDTO.setCourseId(dto.getCourseId());
            attendanceDTO.setDate(dto.getDate());
            attendanceDTO.setStatus(record.getStatus());
            attendanceDTO.setRemarks(record.getRemarks());
            attendanceDTO.setSessionType(dto.getSessionType());

            Attendance saved = markAttendance(attendanceDTO, markedBy);
            attendanceList.add(saved);
        }

        return attendanceList;
    }

    public List<Attendance> getAttendanceByStudentAndCourse(String studentId, String courseId) {
        return attendanceRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    public List<Attendance> getAttendanceByClassAndDate(String classId, LocalDate date) {
        return attendanceRepository.findByClassIdAndDate(classId, date);
    }

    public List<Attendance> getAttendanceByCourseAndDate(String courseId, LocalDate date) {
        return attendanceRepository.findByCourseIdAndDate(courseId, date);
    }

    public List<AttendanceSummary> getStudentAttendanceSummary(String studentId, String academicYear, String semester) {
        return attendanceSummaryRepository.findByStudentIdAndAcademicYearAndSemester(studentId, academicYear, semester);
    }

    public AttendanceReportDTO getStudentAttendanceReport(String studentId, String courseId) {
        AttendanceReportDTO report = new AttendanceReportDTO();
        report.setStudentId(studentId);
        report.setCourseId(courseId);

        // Get student name
        Student student = studentRepository.findByStudentId(studentId);
        if (student != null) {
            report.setStudentName(student.getName());
        }

        // Get course name
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            report.setCourseName(courseOpt.get().getCourseTitle());
        }

        // Calculate counts
        long presentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.PRESENT);
        long absentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.ABSENT);
        long lateCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.LATE);
        long excusedCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.EXCUSED);

        int total = (int) (presentCount + absentCount + lateCount + excusedCount);

        report.setPresent((int) presentCount);
        report.setAbsent((int) absentCount);
        report.setLate((int) lateCount);
        report.setExcused((int) excusedCount);
        report.setTotalClasses(total);
        report.setPercentage(total > 0 ? ((double) (presentCount + lateCount) / total) * 100.0 : 0.0);

        return report;
    }

    public List<AttendanceSummary> getLowAttendanceStudents(double threshold) {
        List<AttendanceSummary> allSummaries = attendanceSummaryRepository.findAll();
        return allSummaries.stream()
                .filter(s -> s.getAttendancePercentage() < threshold)
                .collect(Collectors.toList());
    }

    public void updateAttendanceSummary(String studentId, String courseId) {
        long presentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.PRESENT);
        long absentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.ABSENT);
        long lateCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.LATE);
        long excusedCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, Attendance.AttendanceStatus.EXCUSED);

        int total = (int) (presentCount + absentCount + lateCount + excusedCount);
        double percentage = total > 0 ? ((double) (presentCount + lateCount) / total) * 100.0 : 0.0;

        Optional<AttendanceSummary> existingOpt = attendanceSummaryRepository.findByStudentIdAndCourseId(studentId, courseId);

        AttendanceSummary summary;
        if (existingOpt.isPresent()) {
            summary = existingOpt.get();
        } else {
            summary = new AttendanceSummary();
            summary.setStudentId(studentId);
            summary.setCourseId(courseId);

            // Set academic year and semester from course
            Optional<Course> courseOpt = courseRepository.findById(courseId);
            if (courseOpt.isPresent()) {
                summary.setSemester(courseOpt.get().getSemester());
                summary.setAcademicYear(String.valueOf(LocalDate.now().getYear()));
            }
        }

        summary.setTotalClasses(total);
        summary.setPresentCount((int) presentCount);
        summary.setAbsentCount((int) absentCount);
        summary.setLateCount((int) lateCount);
        summary.setExcusedCount((int) excusedCount);
        summary.setAttendancePercentage(percentage);
        summary.setLastUpdated(LocalDateTime.now());

        attendanceSummaryRepository.save(summary);
    }

    public List<Attendance> getAttendanceByDateRange(String studentId, String courseId, LocalDate start, LocalDate end) {
        return attendanceRepository.findByStudentIdAndCourseIdAndDateBetween(studentId, courseId, start, end);
    }
}
