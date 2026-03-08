package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.AdmissionApplication;
import com.example.resultmanagementsystem.model.AdmissionApplication.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionApplicationRepository extends MongoRepository<AdmissionApplication, String> {

    Optional<AdmissionApplication> findByApplicantEmail(String applicantEmail);

    List<AdmissionApplication> findByStatus(ApplicationStatus status);

    Page<AdmissionApplication> findByStatus(ApplicationStatus status, Pageable pageable);

    List<AdmissionApplication> findByDepartmentId(String departmentId);

    List<AdmissionApplication> findByAcademicYear(String academicYear);

    List<AdmissionApplication> findByStatusAndDepartmentId(ApplicationStatus status, String departmentId);

    long countByStatus(ApplicationStatus status);

    long countByStatusAndAcademicYear(ApplicationStatus status, String academicYear);
}
