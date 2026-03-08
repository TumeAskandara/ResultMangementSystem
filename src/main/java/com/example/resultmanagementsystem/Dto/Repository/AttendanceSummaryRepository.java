package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.AttendanceSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSummaryRepository extends MongoRepository<AttendanceSummary, String> {

    Optional<AttendanceSummary> findByStudentIdAndCourseId(String studentId, String courseId);

    List<AttendanceSummary> findByStudentIdAndAcademicYearAndSemester(String studentId, String academicYear, String semester);

    List<AttendanceSummary> findByStudentIdAndAttendancePercentageLessThan(String studentId, double threshold);
}
