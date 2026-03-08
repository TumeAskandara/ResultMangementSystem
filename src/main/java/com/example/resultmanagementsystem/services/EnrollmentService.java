package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.EnrollmentDTO;
import com.example.resultmanagementsystem.Dto.Repository.EnrollmentRepository;
import com.example.resultmanagementsystem.model.Enrollment;
import com.example.resultmanagementsystem.model.Enrollment.EnrollmentStatus;
import com.example.resultmanagementsystem.model.Enrollment.EnrollmentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public Enrollment enrollStudent(EnrollmentDTO dto) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(dto.getStudentId());
        enrollment.setAdmissionApplicationId(dto.getAdmissionApplicationId());
        enrollment.setDepartmentId(dto.getDepartmentId());
        enrollment.setAcademicYear(dto.getAcademicYear());
        enrollment.setSemester(dto.getSemester());
        enrollment.setEnrollmentType(EnrollmentType.valueOf(dto.getEnrollmentType()));
        enrollment.setClassId(dto.getClassId());
        enrollment.setSection(dto.getSection());
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment getEnrollmentById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public Page<Enrollment> getEnrollmentsByDepartment(String departmentId, Pageable pageable) {
        return enrollmentRepository.findByDepartmentId(departmentId, pageable);
    }

    public List<Enrollment> getEnrollmentsByClass(String classId) {
        return enrollmentRepository.findByClassId(classId);
    }

    public Enrollment updateEnrollmentStatus(String id, String status) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setStatus(EnrollmentStatus.valueOf(status));
        enrollment.setUpdatedAt(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);
    }

    public Map<String, Long> getEnrollmentStatistics(String academicYear) {
        Map<String, Long> statistics = new HashMap<>();
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            long count = enrollmentRepository.countByStatusAndAcademicYear(status, academicYear);
            statistics.put(status.name(), count);
        }
        return statistics;
    }

    public Page<Enrollment> getActiveEnrollmentsByAcademicYear(String year, Pageable pageable) {
        return enrollmentRepository.findByStatusAndAcademicYear(EnrollmentStatus.ACTIVE, year, pageable);
    }
}
