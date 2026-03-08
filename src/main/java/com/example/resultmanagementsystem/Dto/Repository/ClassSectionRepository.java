package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.ClassSection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassSectionRepository extends MongoRepository<ClassSection, String> {
    List<ClassSection> findByDepartmentIdAndAcademicYear(String departmentId, String academicYear);
    List<ClassSection> findByClassTeacherId(String classTeacherId);
    List<ClassSection> findByIsActiveAndAcademicYear(boolean isActive, String academicYear);
    List<ClassSection> findByStudentIdsContaining(String studentId);
}
