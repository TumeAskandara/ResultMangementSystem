package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.AdmissionApplicationDTO;
import com.example.resultmanagementsystem.Dto.AdmissionReviewDTO;
import com.example.resultmanagementsystem.Dto.Repository.AdmissionApplicationRepository;
import com.example.resultmanagementsystem.model.AdmissionApplication;
import com.example.resultmanagementsystem.model.AdmissionApplication.ApplicationStatus;
import com.example.resultmanagementsystem.model.AdmissionApplication.Gender;
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
public class AdmissionService {

    private final AdmissionApplicationRepository admissionApplicationRepository;

    public AdmissionApplication applyForAdmission(AdmissionApplicationDTO dto) {
        AdmissionApplication application = new AdmissionApplication();
        application.setApplicantFirstName(dto.getApplicantFirstName());
        application.setApplicantLastName(dto.getApplicantLastName());
        application.setApplicantEmail(dto.getApplicantEmail());
        application.setApplicantPhone(dto.getApplicantPhone());
        application.setDateOfBirth(dto.getDateOfBirth());
        application.setGender(Gender.valueOf(dto.getGender()));
        application.setNationality(dto.getNationality());
        application.setAddress(dto.getAddress());
        application.setPreviousSchool(dto.getPreviousSchool());
        application.setPreviousGrade(dto.getPreviousGrade());
        application.setDepartmentId(dto.getDepartmentId());
        application.setAcademicYear(dto.getAcademicYear());
        application.setEmergencyContactName(dto.getEmergencyContactName());
        application.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        application.setParentGuardianName(dto.getParentGuardianName());
        application.setParentGuardianEmail(dto.getParentGuardianEmail());
        application.setParentGuardianPhone(dto.getParentGuardianPhone());
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        return admissionApplicationRepository.save(application);
    }

    public AdmissionApplication getApplicationById(String id) {
        return admissionApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admission application not found with id: " + id));
    }

    public Page<AdmissionApplication> getApplicationsByStatus(String status, Pageable pageable) {
        ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status);
        return admissionApplicationRepository.findByStatus(applicationStatus, pageable);
    }

    public List<AdmissionApplication> getApplicationsByDepartment(String departmentId) {
        return admissionApplicationRepository.findByDepartmentId(departmentId);
    }

    public List<AdmissionApplication> getApplicationsByAcademicYear(String year) {
        return admissionApplicationRepository.findByAcademicYear(year);
    }

    public AdmissionApplication reviewApplication(String id, AdmissionReviewDTO reviewDTO) {
        AdmissionApplication application = getApplicationById(id);
        application.setStatus(ApplicationStatus.valueOf(reviewDTO.getStatus()));
        application.setReviewNotes(reviewDTO.getReviewNotes());
        application.setReviewDate(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        return admissionApplicationRepository.save(application);
    }

    public Map<String, Long> getAdmissionStatistics(String academicYear) {
        Map<String, Long> statistics = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            long count = admissionApplicationRepository.countByStatusAndAcademicYear(status, academicYear);
            statistics.put(status.name(), count);
        }
        return statistics;
    }

    public AdmissionApplication getApplicationByEmail(String email) {
        return admissionApplicationRepository.findByApplicantEmail(email)
                .orElseThrow(() -> new RuntimeException("Admission application not found with email: " + email));
    }
}
