package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {
    List<Exam> findByCourseId(String courseId);
    List<Exam> findByDepartmentIdContaining(String departmentId);
    List<Exam> findByTeacherId(String teacherId);
    List<Exam> findByStatus(Exam.ExamStatus status);
    List<Exam> findByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime dateTime);
    List<Exam> findByAcademicYearAndSemester(String academicYear, String semester);
}
