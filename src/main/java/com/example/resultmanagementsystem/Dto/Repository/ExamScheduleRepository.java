package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.ExamSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamScheduleRepository extends MongoRepository<ExamSchedule, String> {
    List<ExamSchedule> findByDepartmentId(String departmentId);
    List<ExamSchedule> findByDate(LocalDate date);
    List<ExamSchedule> findByExamId(String examId);
    List<ExamSchedule> findByInvigilatorId(String invigilatorId);
    List<ExamSchedule> findByAcademicYearAndSemester(String academicYear, String semester);
}
