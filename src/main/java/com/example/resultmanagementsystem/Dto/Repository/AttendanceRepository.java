package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByStudentIdAndCourseId(String studentId, String courseId);

    List<Attendance> findByStudentIdAndDate(String studentId, LocalDate date);

    List<Attendance> findByClassIdAndDate(String classId, LocalDate date);

    List<Attendance> findByCourseIdAndDate(String courseId, LocalDate date);

    List<Attendance> findByStudentIdAndCourseIdAndDateBetween(String studentId, String courseId, LocalDate startDate, LocalDate endDate);

    long countByStudentIdAndCourseIdAndStatus(String studentId, String courseId, Attendance.AttendanceStatus status);

    List<Attendance> findByTeacherIdAndDate(String teacherId, LocalDate date);

    List<Attendance> findByDepartmentIdAndDate(String departmentId, LocalDate date);
}
