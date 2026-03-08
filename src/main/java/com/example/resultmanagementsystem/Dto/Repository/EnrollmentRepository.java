package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Enrollment;
import com.example.resultmanagementsystem.model.Enrollment.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    List<Enrollment> findByStudentId(String studentId);

    List<Enrollment> findByDepartmentId(String departmentId);

    Page<Enrollment> findByDepartmentId(String departmentId, Pageable pageable);

    List<Enrollment> findByAcademicYear(String academicYear);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    List<Enrollment> findByClassId(String classId);

    List<Enrollment> findByStudentIdAndAcademicYear(String studentId, String academicYear);

    long countByStatusAndAcademicYear(EnrollmentStatus status, String academicYear);

    Page<Enrollment> findByStatusAndAcademicYear(EnrollmentStatus status, String academicYear, Pageable pageable);
}
