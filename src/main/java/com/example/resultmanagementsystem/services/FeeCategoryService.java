package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.FeeCategoryRepository;
import com.example.resultmanagementsystem.model.FeeCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeeCategoryService {

    private final FeeCategoryRepository feeCategoryRepository;

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new fee category
     */
    public FeeCategory createFeeCategory(FeeCategory feeCategory) {
        log.info("Creating fee category: {}", feeCategory.getName());

        // Generate ID if not provided
        if (feeCategory.getCategoryId() == null || feeCategory.getCategoryId().isEmpty()) {
            feeCategory.setCategoryId(UUID.randomUUID().toString());
        }

        // Validate required fields
        validateFeeCategory(feeCategory);

        // Check for duplicate names in same academic year
        if (isDuplicateFeeCategoryName(feeCategory.getName(), feeCategory.getAcademicYear(), null)) {
            throw new RuntimeException("Fee category with name '" + feeCategory.getName() +
                    "' already exists for academic year " + feeCategory.getAcademicYear());
        }

        FeeCategory saved = feeCategoryRepository.save(feeCategory);
        log.info("Fee category created successfully with ID: {}", saved.getCategoryId());
        return saved;
    }

    /**
     * Get all fee categories with pagination
     */
    public Page<FeeCategory> getAllFeeCategories(Pageable pageable) {
        log.debug("Retrieving all fee categories with pagination");
        return feeCategoryRepository.findAll(pageable);
    }

    /**
     * Get fee category by ID
     */
    public FeeCategory getFeeCategoryById(String categoryId) {
        log.debug("Retrieving fee category with ID: {}", categoryId);
        return feeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Fee category not found with ID: " + categoryId));
    }

    /**
     * Update fee category
     */
    public FeeCategory updateFeeCategory(String categoryId, FeeCategory updatedCategory) {
        log.info("Updating fee category with ID: {}", categoryId);

        FeeCategory existing = getFeeCategoryById(categoryId);

        // Validate updated data
        validateFeeCategory(updatedCategory);

        // Check for duplicate names (excluding current category)
        if (isDuplicateFeeCategoryName(updatedCategory.getName(), updatedCategory.getAcademicYear(), categoryId)) {
            throw new RuntimeException("Fee category with name '" + updatedCategory.getName() +
                    "' already exists for academic year " + updatedCategory.getAcademicYear());
        }

        // Update fields
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        existing.setAmount(updatedCategory.getAmount());
        existing.setDepartmentId(updatedCategory.getDepartmentId());
        existing.setAcademicYear(updatedCategory.getAcademicYear());
        existing.setMandatory(updatedCategory.isMandatory());

        FeeCategory saved = feeCategoryRepository.save(existing);
        log.info("Fee category updated successfully");
        return saved;
    }

    /**
     * Delete fee category
     */
    public void deleteFeeCategory(String categoryId) {
        log.info("Deleting fee category with ID: {}", categoryId);

        FeeCategory category = getFeeCategoryById(categoryId);

        // Check if category is being used (you might want to add this validation)
        // if (isCategoryInUse(categoryId)) {
        //     throw new RuntimeException("Cannot delete fee category as it's being used in fee records");
        // }

        feeCategoryRepository.delete(category);
        log.info("Fee category deleted successfully");
    }

    // ==================== QUERY OPERATIONS ====================

    /**
     * Get fee categories by academic year
     */
    public List<FeeCategory> getFeeCategories(String academicYear) {
        log.debug("Retrieving fee categories for academic year: {}", academicYear);
        return feeCategoryRepository.findByAcademicYear(academicYear);
    }

    // In your FeeCategory service class
    public List<FeeCategory> getFeeCategoriesByDepartmentId(String departmentId) {
        return getFeeCategoriesByDepartmentId("2024-2025", departmentId);
    }

    public List<FeeCategory> getFeeCategoriesByDepartmentId(String academicYear, String departmentId) {
        log.debug("Retrieving fee categories for academic year: {} and department: {}", academicYear, departmentId);
        return feeCategoryRepository.findByAcademicYearAndDepartmentId(academicYear, departmentId);
    }

    /**
     * Get applicable fee categories for a student (existing method)
     */
    public List<FeeCategory> getApplicableFeeCategories(String departmentId, String academicYear) {
        log.debug("Retrieving applicable fee categories for department: {} and year: {}", departmentId, academicYear);
        return feeCategoryRepository.findByAcademicYearAndDepartmentIdInOrDepartmentIdIsNull(
                academicYear, List.of(departmentId)
        );
    }

    /**
     * Get mandatory fee categories for an academic year
     */
    public List<FeeCategory> getMandatoryFeeCategories(String academicYear) {
        log.debug("Retrieving mandatory fee categories for year: {}", academicYear);
        return feeCategoryRepository.findByAcademicYearAndMandatory(academicYear, true);
    }

    /**
     * Get optional fee categories for an academic year
     */
    public List<FeeCategory> getOptionalFeeCategories(String academicYear) {
        log.debug("Retrieving optional fee categories for year: {}", academicYear);
        return feeCategoryRepository.findByAcademicYearAndMandatory(academicYear, false);
    }

    // ==================== CALCULATION METHODS ====================

    /**
     * Calculate total mandatory fees for a student (existing method)
     */
    public Double calculateMandatoryFeesForStudent(String departmentId, String academicYear) {
        List<FeeCategory> categories = getApplicableFeeCategories(departmentId, academicYear);
        return categories.stream()
                .filter(FeeCategory::isMandatory)
                .mapToDouble(FeeCategory::getAmount)
                .sum();
    }

    /**
     * Calculate total optional fees for a student
     */
    public Double calculateOptionalFeesForStudent(String departmentId, String academicYear) {
        List<FeeCategory> categories = getApplicableFeeCategories(departmentId, academicYear);
        return categories.stream()
                .filter(category -> !category.isMandatory())
                .mapToDouble(FeeCategory::getAmount)
                .sum();
    }

    /**
     * Get fee breakdown summary
     */
    public Map<String, Object> getFeeBreakdownSummary(String departmentId, String academicYear) {
        List<FeeCategory> categories = getApplicableFeeCategories(departmentId, academicYear);

        Double mandatoryTotal = categories.stream()
                .filter(FeeCategory::isMandatory)
                .mapToDouble(FeeCategory::getAmount)
                .sum();

        Double optionalTotal = categories.stream()
                .filter(category -> !category.isMandatory())
                .mapToDouble(FeeCategory::getAmount)
                .sum();

        List<FeeCategory> mandatoryCategories = categories.stream()
                .filter(FeeCategory::isMandatory)
                .collect(Collectors.toList());

        List<FeeCategory> optionalCategories = categories.stream()
                .filter(category -> !category.isMandatory())
                .collect(Collectors.toList());

        Map<String, Object> summary = new HashMap<>();
        summary.put("departmentId", departmentId);
        summary.put("academicYear", academicYear);
        summary.put("mandatoryTotal", mandatoryTotal);
        summary.put("optionalTotal", optionalTotal);
        summary.put("grandTotal", mandatoryTotal + optionalTotal);
        summary.put("mandatoryCategories", mandatoryCategories);
        summary.put("optionalCategories", optionalCategories);
        summary.put("totalCategories", categories.size());

        return summary;
    }

    // ==================== BULK OPERATIONS ====================

    /**
     * Create multiple fee categories
     */
    public List<FeeCategory> createBulkFeeCategories(List<FeeCategory> feeCategories) {
        log.info("Creating {} fee categories in bulk", feeCategories.size());

        List<FeeCategory> result = new ArrayList<>();

        for (FeeCategory category : feeCategories) {
            try {
                result.add(createFeeCategory(category));
            } catch (Exception e) {
                log.error("Failed to create fee category: {}, Error: {}", category.getName(), e.getMessage());
                // You might want to collect errors and return them
            }
        }

        log.info("Successfully created {} out of {} fee categories", result.size(), feeCategories.size());
        return result;
    }

    /**
     * Bulk update fee amounts
     */
    public void bulkUpdateFeeAmounts(String academicYear, Map<String, Double> categoryAmounts) {
        log.info("Bulk updating fee amounts for academic year: {}", academicYear);

        List<FeeCategory> categories = getFeeCategories(academicYear);

        for (FeeCategory category : categories) {
            if (categoryAmounts.containsKey(category.getName())) {
                category.setAmount(categoryAmounts.get(category.getName()));
                feeCategoryRepository.save(category);
                log.debug("Updated amount for category: {} to {}", category.getName(), category.getAmount());
            }
        }

        log.info("Bulk update completed");
    }

    // ==================== ADMIN OPERATIONS ====================

    /**
     * Setup school fee structure (enhanced to accept academic year)
     */
    public void setupSchoolFeeStructure(String academicYear) {
        log.info("Setting up fee structure for academic year: {}", academicYear);

        // Check if fee structure already exists
        List<FeeCategory> existing = getFeeCategories(academicYear);
        if (!existing.isEmpty()) {
            log.warn("Fee structure already exists for academic year: {}", academicYear);
            return;
        }

        // Create default fee categories
        createFeeCategory("Tuition Fee", "Main academic fee", 120000.0, null, academicYear, true);
        createFeeCategory("Library Fee", "Access to library resources", 15000.0, null, academicYear, true);
        createFeeCategory("Sports Fee", "Sports activities and facilities", 10000.0, null, academicYear, false);
        createFeeCategory("Lab Fee", "Laboratory usage", 25000.0, "ENGINEERING", academicYear, true);
        createFeeCategory("Hostel Fee", "Accommodation", 80000.0, null, academicYear, false);
        createFeeCategory("Exam Fee", "Examination costs", 20000.0, null, academicYear, true);

        log.info("Fee structure setup completed for academic year: {}", academicYear);
    }

    /**
     * Clone fee structure from previous year
     */
    public void cloneFeeStructure(String fromYear, String toYear, Double inflationRate) {
        log.info("Cloning fee structure from {} to {} with inflation rate: {}", fromYear, toYear, inflationRate);

        List<FeeCategory> sourceCategories = getFeeCategories(fromYear);
        if (sourceCategories.isEmpty()) {
            throw new RuntimeException("No fee categories found for source academic year: " + fromYear);
        }

        // Check if target year already has categories
        List<FeeCategory> targetCategories = getFeeCategories(toYear);
        if (!targetCategories.isEmpty()) {
            throw new RuntimeException("Fee categories already exist for target academic year: " + toYear);
        }

        for (FeeCategory source : sourceCategories) {
            FeeCategory cloned = new FeeCategory();
            cloned.setCategoryId(UUID.randomUUID().toString());
            cloned.setName(source.getName());
            cloned.setDescription(source.getDescription());
            cloned.setAmount(source.getAmount() * inflationRate); // Apply inflation
            cloned.setDepartmentId(source.getDepartmentId());
            cloned.setAcademicYear(toYear);
            cloned.setMandatory(source.isMandatory());

            feeCategoryRepository.save(cloned);
        }

        log.info("Successfully cloned {} fee categories", sourceCategories.size());
    }

    /**
     * Archive fee categories (mark as inactive)
     */
    public void archiveFeeCategories(String academicYear) {
        log.info("Archiving fee categories for academic year: {}", academicYear);

        List<FeeCategory> categories = getFeeCategories(academicYear);

        // You might want to add an "archived" or "active" field to your FeeCategory model
        // For now, we'll just log the action
        log.info("Would archive {} fee categories for year: {}", categories.size(), academicYear);

        // Implementation would depend on how you want to handle archiving
        // Option 1: Add an "archived" boolean field
        // Option 2: Move to a separate collection
        // Option 3: Soft delete with a "deleted_at" timestamp
    }

    // ==================== SEARCH AND FILTER ====================

    /**
     * Search fee categories by name or description
     */
    public List<FeeCategory> searchFeeCategories(String query, String year) {
        log.debug("Searching fee categories with query: {} for year: {}", query, year);

        // This would require custom repository methods
        // For now, we'll do a simple filter
        List<FeeCategory> allCategories = year != null ?
                getFeeCategories(year) : feeCategoryRepository.findAll();

        return allCategories.stream()
                .filter(category ->
                        category.getName().toLowerCase().contains(query.toLowerCase()) ||
                                category.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Filter fee categories by multiple criteria
     */
    public List<FeeCategory> filterFeeCategories(Map<String, Object> filterCriteria) {
        log.debug("Filtering fee categories with criteria: {}", filterCriteria);

        // This is a simplified implementation
        // In a real application, you'd want to use MongoDB Query/Criteria API
        List<FeeCategory> allCategories = feeCategoryRepository.findAll();

        return allCategories.stream()
                .filter(category -> matchesFilter(category, filterCriteria))
                .collect(Collectors.toList());
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Create fee category with explicit parameters (existing method)
     */
    private void createFeeCategory(String name, String description, Double amount,
                                   String departmentId, String academicYear, boolean mandatory) {
        FeeCategory category = new FeeCategory();
        category.setCategoryId(UUID.randomUUID().toString());
        category.setName(name);
        category.setDescription(description);
        category.setAmount(amount);
        category.setDepartmentId(departmentId);
        category.setAcademicYear(academicYear);
        category.setMandatory(mandatory);

        feeCategoryRepository.save(category);
    }

    /**
     * Validate fee category data
     */
    private void validateFeeCategory(FeeCategory feeCategory) {
        if (feeCategory.getName() == null || feeCategory.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Fee category name is required");
        }

        if (feeCategory.getAmount() == null || feeCategory.getAmount() <= 0) {
            throw new IllegalArgumentException("Fee amount must be greater than 0");
        }

        if (feeCategory.getAcademicYear() == null || feeCategory.getAcademicYear().trim().isEmpty()) {
            throw new IllegalArgumentException("Academic year is required");
        }
    }

    /**
     * Check for duplicate fee category names
     */
    private boolean isDuplicateFeeCategoryName(String name, String academicYear, String excludeCategoryId) {
        List<FeeCategory> existing = getFeeCategories(academicYear);
        return existing.stream()
                .anyMatch(category ->
                        category.getName().equalsIgnoreCase(name) &&
                                !category.getCategoryId().equals(excludeCategoryId));
    }

    /**
     * Check if filter criteria matches a category
     */
    private boolean matchesFilter(FeeCategory category, Map<String, Object> filterCriteria) {
        for (Map.Entry<String, Object> entry : filterCriteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "academicYear":
                    if (!category.getAcademicYear().equals(value)) return false;
                    break;
                case "departmentId":
                    if (!Objects.equals(category.getDepartmentId(), value)) return false;
                    break;
                case "mandatory":
                    if (category.isMandatory() != (Boolean) value) return false;
                    break;
                case "minAmount":
                    if (category.getAmount() < (Double) value) return false;
                    break;
                case "maxAmount":
                    if (category.getAmount() > (Double) value) return false;
                    break;
            }
        }
        return true;
    }
}