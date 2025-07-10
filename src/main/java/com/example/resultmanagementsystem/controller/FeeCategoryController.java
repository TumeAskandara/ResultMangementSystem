package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.model.FeeCategory;
import com.example.resultmanagementsystem.services.FeeCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/fee-categories")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FeeCategoryController {

    private final FeeCategoryService feeCategoryService;

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new fee category
     * POST /api/fee-categories
     */
    @PostMapping
    public ResponseEntity<FeeCategory> createFeeCategory(@Valid @RequestBody FeeCategory feeCategory) {
        log.info("Creating new fee category: {}", feeCategory.getName());
        FeeCategory created = feeCategoryService.createFeeCategory(feeCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all fee categories with pagination
     * GET /api/fee-categories?page=0&size=10&sort=name
     */
    @GetMapping
    public ResponseEntity<Page<FeeCategory>> getAllFeeCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FeeCategory> categories = feeCategoryService.getAllFeeCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get fee category by ID
     * GET /api/fee-categories/{id}
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<FeeCategory> getFeeCategoryById(@PathVariable String categoryId) {
        log.debug("Retrieving fee category with ID: {}", categoryId);
        FeeCategory category = feeCategoryService.getFeeCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    /**
     * Update fee category
     * PUT /api/fee-categories/{id}
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<FeeCategory> updateFeeCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody FeeCategory feeCategory) {
        log.info("Updating fee category with ID: {}", categoryId);
        FeeCategory updated = feeCategoryService.updateFeeCategory(categoryId, feeCategory);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete fee category
     * DELETE /api/fee-categories/{id}
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, String>> deleteFeeCategory(@PathVariable String categoryId) {
        log.info("Deleting fee category with ID: {}", categoryId);
        feeCategoryService.deleteFeeCategory(categoryId);
        return ResponseEntity.ok(Map.of("message", "Fee category deleted successfully"));
    }

    // ==================== QUERY OPERATIONS ====================

    /**
     * Get fee categories by academic year
     * GET /api/fee-categories/year/{academicYear}
     */
    @GetMapping("/year/{academicYear}")
    public ResponseEntity<List<FeeCategory>> getFeeCategories(@PathVariable String academicYear) {
        log.debug("Retrieving fee categories for academic year: {}", academicYear);
        List<FeeCategory> categories = feeCategoryService.getFeeCategories(academicYear);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get fee categories for specific department and academic year
     * GET /api/fee-categories/department/{departmentId}/year/{academicYear}
     */
    @GetMapping("/department/{departmentId}/year/{academicYear}")
    public ResponseEntity<List<FeeCategory>> getDepartmentFeeCategories(
            @PathVariable String departmentId,
            @PathVariable String academicYear) {
        log.debug("Retrieving fee categories for department: {} and year: {}", departmentId, academicYear);
        List<FeeCategory> categories = feeCategoryService.getApplicableFeeCategories(departmentId, academicYear);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get mandatory fee categories only
     * GET /api/fee-categories/mandatory/year/{academicYear}
     */
    @GetMapping("/mandatory/year/{academicYear}")
    public ResponseEntity<List<FeeCategory>> getMandatoryFeeCategories(@PathVariable String academicYear) {
        log.debug("Retrieving mandatory fee categories for year: {}", academicYear);
        List<FeeCategory> categories = feeCategoryService.getMandatoryFeeCategories(academicYear);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get optional fee categories only
     * GET /api/fee-categories/optional/year/{academicYear}
     */
    @GetMapping("/optional/year/{academicYear}")
    public ResponseEntity<List<FeeCategory>> getOptionalFeeCategories(@PathVariable String academicYear) {
        log.debug("Retrieving optional fee categories for year: {}", academicYear);
        List<FeeCategory> categories = feeCategoryService.getOptionalFeeCategories(academicYear);
        return ResponseEntity.ok(categories);
    }

    // ==================== CALCULATION ENDPOINTS ====================

    /**
     * Calculate total mandatory fees for a department
     * GET /api/fee-categories/mandatory-total/department/{departmentId}/year/{academicYear}
     */
    @GetMapping("/mandatory-total/department/{departmentId}/year/{academicYear}")
    public ResponseEntity<Map<String, Double>> getMandatoryFeesTotal(
            @PathVariable String departmentId,
            @PathVariable String academicYear) {
        log.debug("Calculating mandatory fees for department: {} and year: {}", departmentId, academicYear);
        Double total = feeCategoryService.calculateMandatoryFeesForStudent(departmentId, academicYear);
        return ResponseEntity.ok(Map.of("totalMandatoryFees", total));
    }

    /**
     * Calculate total optional fees for a department
     * GET /api/fee-categories/optional-total/department/{departmentId}/year/{academicYear}
     */
    @GetMapping("/optional-total/department/{departmentId}/year/{academicYear}")
    public ResponseEntity<Map<String, Double>> getOptionalFeesTotal(
            @PathVariable String departmentId,
            @PathVariable String academicYear) {
        log.debug("Calculating optional fees for department: {} and year: {}", departmentId, academicYear);
        Double total = feeCategoryService.calculateOptionalFeesForStudent(departmentId, academicYear);
        return ResponseEntity.ok(Map.of("totalOptionalFees", total));
    }

    /**
     * Get fee breakdown summary
     * GET /api/fee-categories/summary/department/{departmentId}/year/{academicYear}
     */
    @GetMapping("/summary/department/{departmentId}/year/{academicYear}")
    public ResponseEntity<Map<String, Object>> getFeeBreakdownSummary(
            @PathVariable String departmentId,
            @PathVariable String academicYear) {
        log.debug("Getting fee breakdown summary for department: {} and year: {}", departmentId, academicYear);
        Map<String, Object> summary = feeCategoryService.getFeeBreakdownSummary(departmentId, academicYear);
        return ResponseEntity.ok(summary);
    }

    // ==================== BULK OPERATIONS ====================

    /**
     * Create multiple fee categories at once
     * POST /api/fee-categories/bulk
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<FeeCategory>> createBulkFeeCategories(
            @Valid @RequestBody List<FeeCategory> feeCategories) {
        log.info("Creating {} fee categories in bulk", feeCategories.size());
        List<FeeCategory> created = feeCategoryService.createBulkFeeCategories(feeCategories);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update fee amounts for an academic year (bulk price update)
     * PUT /api/fee-categories/bulk-update-amounts/year/{academicYear}
     */
    @PutMapping("/bulk-update-amounts/year/{academicYear}")
    public ResponseEntity<Map<String, String>> bulkUpdateFeeAmounts(
            @PathVariable String academicYear,
            @RequestBody Map<String, Double> categoryAmounts) {
        log.info("Bulk updating fee amounts for academic year: {}", academicYear);
        feeCategoryService.bulkUpdateFeeAmounts(academicYear, categoryAmounts);
        return ResponseEntity.ok(Map.of("message", "Fee amounts updated successfully"));
    }

    // ==================== ADMIN OPERATIONS ====================

    /**
     * Setup initial school fee structure (for new academic year)
     * POST /api/fee-categories/setup-fee-structure
     */
    @PostMapping("/setup-fee-structure")
    public ResponseEntity<Map<String, String>> setupFeeStructure(
            @RequestParam(required = false, defaultValue = "2024-2025") String academicYear) {
        log.info("Setting up fee structure for academic year: {}", academicYear);
        feeCategoryService.setupSchoolFeeStructure(academicYear);
        return ResponseEntity.ok(Map.of("message", "Fee structure setup completed for " + academicYear));
    }

    /**
     * Clone fee structure from previous year
     * POST /api/fee-categories/clone-structure
     */
    @PostMapping("/clone-structure")
    public ResponseEntity<Map<String, String>> cloneFeeStructure(
            @RequestParam String fromYear,
            @RequestParam String toYear,
            @RequestParam(required = false, defaultValue = "1.0") Double inflationRate) {
        log.info("Cloning fee structure from {} to {} with inflation rate: {}", fromYear, toYear, inflationRate);
        feeCategoryService.cloneFeeStructure(fromYear, toYear, inflationRate);
        return ResponseEntity.ok(Map.of("message", "Fee structure cloned successfully"));
    }

    /**
     * Archive fee categories for completed academic year
     * PUT /api/fee-categories/archive/year/{academicYear}
     */
    @PutMapping("/archive/year/{academicYear}")
    public ResponseEntity<Map<String, String>> archiveFeeCategories(@PathVariable String academicYear) {
        log.info("Archiving fee categories for academic year: {}", academicYear);
        feeCategoryService.archiveFeeCategories(academicYear);
        return ResponseEntity.ok(Map.of("message", "Fee categories archived successfully"));
    }

    // ==================== SEARCH AND FILTER ====================

    /**
     * Search fee categories by name or description
     * GET /api/fee-categories/search?query=tuition&year=2024-2025
     */
    @GetMapping("/search")
    public ResponseEntity<List<FeeCategory>> searchFeeCategories(
            @RequestParam String query,
            @RequestParam(required = false) String year) {
        log.debug("Searching fee categories with query: {} for year: {}", query, year);
        List<FeeCategory> categories = feeCategoryService.searchFeeCategories(query, year);
        return ResponseEntity.ok(categories);
    }

    /**
     * Filter fee categories by multiple criteria
     * POST /api/fee-categories/filter
     */
    @PostMapping("/filter")
    public ResponseEntity<List<FeeCategory>> filterFeeCategories(
            @RequestBody Map<String, Object> filterCriteria) {
        log.debug("Filtering fee categories with criteria: {}", filterCriteria);
        List<FeeCategory> categories = feeCategoryService.filterFeeCategories(filterCriteria);
        return ResponseEntity.ok(categories);
    }
}