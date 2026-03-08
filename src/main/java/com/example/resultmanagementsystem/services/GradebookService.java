package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.*;
import com.example.resultmanagementsystem.Dto.Repository.GradeAppealRepository;
import com.example.resultmanagementsystem.Dto.Repository.GradeCategoryRepository;
import com.example.resultmanagementsystem.Dto.Repository.GradeScaleRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.model.GradeAppeal;
import com.example.resultmanagementsystem.model.GradeCategory;
import com.example.resultmanagementsystem.model.GradeScale;
import com.example.resultmanagementsystem.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GradebookService {

    private final GradeCategoryRepository gradeCategoryRepository;
    private final GradeScaleRepository gradeScaleRepository;
    private final GradeAppealRepository gradeAppealRepository;
    private final ResultRepository resultRepository;

    // ========== Grade Category Operations ==========

    public GradeCategory createGradeCategory(GradeCategoryDTO dto) {
        GradeCategory category = new GradeCategory();
        category.setId(UUID.randomUUID().toString());
        category.setDepartmentId(dto.getDepartmentId());
        category.setAcademicYear(dto.getAcademicYear());
        category.setCategoryName(dto.getCategoryName());
        category.setWeight(dto.getWeight());
        category.setCourseId(dto.getCourseId());
        category.setCreatedBy(dto.getCreatedBy());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return gradeCategoryRepository.save(category);
    }

    public List<GradeCategory> getCategoriesByCourse(String courseId) {
        return gradeCategoryRepository.findByCourseId(courseId);
    }

    public List<GradeCategory> getCategoriesByDepartment(String departmentId) {
        return gradeCategoryRepository.findByDepartmentId(departmentId);
    }

    public GradeCategory updateGradeCategory(String id, GradeCategoryDTO dto) {
        GradeCategory category = gradeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade category not found with id: " + id));
        category.setDepartmentId(dto.getDepartmentId());
        category.setAcademicYear(dto.getAcademicYear());
        category.setCategoryName(dto.getCategoryName());
        category.setWeight(dto.getWeight());
        category.setCourseId(dto.getCourseId());
        category.setCreatedBy(dto.getCreatedBy());
        category.setUpdatedAt(LocalDateTime.now());
        return gradeCategoryRepository.save(category);
    }

    public void deleteGradeCategory(String id) {
        if (!gradeCategoryRepository.existsById(id)) {
            throw new RuntimeException("Grade category not found with id: " + id);
        }
        gradeCategoryRepository.deleteById(id);
    }

    // ========== Grade Scale Operations ==========

    public GradeScale createGradeScale(GradeScaleDTO dto) {
        GradeScale scale = new GradeScale();
        scale.setId(UUID.randomUUID().toString());
        scale.setName(dto.getName());
        scale.setDepartmentId(dto.getDepartmentId());
        scale.setEntries(dto.getEntries());
        scale.setDefault(dto.isDefault());
        scale.setCreatedAt(LocalDateTime.now());
        return gradeScaleRepository.save(scale);
    }

    public List<GradeScale> getGradeScaleByDepartment(String departmentId) {
        return gradeScaleRepository.findByDepartmentId(departmentId);
    }

    public GradeScale getDefaultGradeScale() {
        return gradeScaleRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new RuntimeException("No default grade scale found"));
    }

    // ========== Grade Appeal Operations ==========

    public GradeAppeal submitGradeAppeal(GradeAppealDTO dto) {
        GradeAppeal appeal = new GradeAppeal();
        appeal.setId(UUID.randomUUID().toString());
        appeal.setStudentId(dto.getStudentId());
        appeal.setCourseId(dto.getCourseId());
        appeal.setResultId(dto.getResultId());
        appeal.setReason(dto.getReason());
        appeal.setCurrentGrade(dto.getCurrentGrade());
        appeal.setRequestedAction(dto.getRequestedAction());
        appeal.setStatus(GradeAppeal.AppealStatus.PENDING);
        appeal.setCreatedAt(LocalDateTime.now());
        return gradeAppealRepository.save(appeal);
    }

    public List<GradeAppeal> getAppealsByStudent(String studentId) {
        return gradeAppealRepository.findByStudentId(studentId);
    }

    public List<GradeAppeal> getAppealsByCourse(String courseId) {
        return gradeAppealRepository.findByCourseId(courseId);
    }

    public Page<GradeAppeal> getPendingAppeals(Pageable pageable) {
        return gradeAppealRepository.findByStatus(GradeAppeal.AppealStatus.PENDING, pageable);
    }

    public GradeAppeal reviewAppeal(String id, GradeAppealReviewDTO dto) {
        GradeAppeal appeal = gradeAppealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade appeal not found with id: " + id));
        appeal.setStatus(dto.getStatus());
        appeal.setReviewedBy(dto.getReviewedBy());
        appeal.setReviewNotes(dto.getReviewNotes());
        appeal.setReviewedAt(LocalDateTime.now());
        return gradeAppealRepository.save(appeal);
    }

    // ========== Weighted Grade Calculation ==========

    public Map<String, Object> calculateWeightedGrade(String studentId, String courseId) {
        List<GradeCategory> categories = gradeCategoryRepository.findByCourseId(courseId);
        List<Result> results = resultRepository.findByStudentId(studentId).stream()
                .filter(r -> r.getCourseId().equals(courseId))
                .toList();

        double weightedTotal = 0.0;
        double totalWeight = 0.0;

        for (GradeCategory category : categories) {
            double categoryWeight = category.getWeight() / 100.0;
            totalWeight += categoryWeight;

            for (Result result : results) {
                double score = result.getTotal();
                weightedTotal += score * categoryWeight;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("courseId", courseId);
        response.put("weightedGrade", totalWeight > 0 ? weightedTotal / results.size() : 0.0);
        response.put("totalWeight", totalWeight * 100);
        response.put("categories", categories);
        return response;
    }
}
