package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.ReportCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCardRepository extends MongoRepository<ReportCard, String> {
    List<ReportCard> findByStudentId(String studentId);
    List<ReportCard> findByDepartmentIdAndAcademicYearAndSemester(String departmentId, String academicYear, String semester);
    List<ReportCard> findByStudentIdAndAcademicYearAndSemester(String studentId, String academicYear, String semester);
    List<ReportCard> findByDepartmentId(String departmentId);
}
