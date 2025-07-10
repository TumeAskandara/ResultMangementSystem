package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.CreateFeeRequest;
import com.example.resultmanagementsystem.Dto.FeeBalanceResponse;
import com.example.resultmanagementsystem.Dto.Repository.FeeCategoryRepository;
import com.example.resultmanagementsystem.Dto.Repository.FeePaymentRepository;
import com.example.resultmanagementsystem.Dto.Repository.FeeRepository;
import com.example.resultmanagementsystem.model.Fee;
import com.example.resultmanagementsystem.model.FeeCategory;
import com.example.resultmanagementsystem.model.FeePayment;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Transactional
public class FeeService {

    // All dependencies injected via constructor (RequiredArgsConstructor)
    private final FeeRepository feeRepository;
    private final FeePaymentRepository feePaymentRepository;
    private final FeeCategoryService feeCategoryService;
    private final StudentService studentService;
    private final MobileMoneyService mobileMoneyService;

    private static final Logger log = LoggerFactory.getLogger(FeeService.class);

    // ==================== FEE RECORD MANAGEMENT ====================

    /**
     * Register a new payment when student brings bank receipt
     */
    @Transactional
    public Fee registerPayment(String studentId, String academicYear, String semester,
                               Double paidAmount, String paymentReference, String paymentMethod) {

        log.info("Registering payment for student: {}, amount: {}, year: {}, semester: {}",
                studentId, paidAmount, academicYear, semester);

        // Validate input
        validatePaymentInput(studentId, academicYear, semester, paidAmount);

        // Check if student exists
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        // Get student's department for fee calculation
        Student studentDepartment = studentService.getStudentDepartmentByStudentId(studentId);
        if (studentDepartment == null) {
            throw new RuntimeException("No department found for student: " + studentId);
        }

        String departmentId = studentDepartment.getDepartmentId();
        if (departmentId == null || departmentId.trim().isEmpty()) {
            throw new RuntimeException("Department ID is null or empty for student: " + studentId);
        }

        // Calculate total fee amount based on department fee categories
        double totalFeeAmount = calculateTotalFeeAmount(departmentId, academicYear);

        // Find existing fee record or create new one
        Optional<Fee> existingFeeOpt = feeRepository.findByStudentIdAndAcademicYearAndSemester(
                studentId, academicYear, semester);

        Fee fee;
        if (existingFeeOpt.isPresent()) {
            // Update existing fee record
            fee = existingFeeOpt.get();
            double currentPaidAmount = fee.getPaidAmount() != null ? fee.getPaidAmount() : 0.0;
            fee.setPaidAmount(currentPaidAmount + paidAmount);
            fee.setPaymentDate(new Date()); // Update to latest payment date
        } else {
            // Create new fee record
            fee = new Fee();
            fee.setStudentId(studentId);
            fee.setAcademicYear(academicYear);
            fee.setSemester(semester);
            fee.setPaidAmount(paidAmount);
            fee.setPaymentDate(new Date());
        }

        // Set total amount and calculate remaining amount
        fee.setTotalAmount(totalFeeAmount);
        double currentPaidAmount = fee.getPaidAmount() != null ? fee.getPaidAmount() : 0.0;
        double remainingAmount = Math.max(totalFeeAmount - currentPaidAmount, 0);
        fee.setRemainingAmount(remainingAmount);

        // Set payment status
        if (remainingAmount <= 0) {
            fee.setStatus(Fee.FeeStatus.valueOf("FULLY_PAID"));
        } else if (currentPaidAmount > 0) {
            fee.setStatus(Fee.FeeStatus.valueOf("PARTIAL_PAYMENT"));
        } else {
            fee.setStatus(Fee.FeeStatus.valueOf("UNPAID"));
        }

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        if (fee.getCreatedAt() == null) {
            fee.setCreatedAt(now);
        }
        fee.setUpdatedAt(now);

        // Save the fee record
        fee = feeRepository.save(fee);

        // Create payment record for tracking individual payments
//        createPaymentRecord(fee, paidAmount, paymentReference, paymentMethod);

        log.info("Payment registered successfully. Total: {}, Paid: {}, Remaining: {}, Status: {}",
                fee.getTotalAmount(), fee.getPaidAmount(), fee.getRemainingAmount(), fee.getStatus());

        return fee;
    }

    /**
     * Create a payment record for tracking individual payments
     */
//    private void createPaymentRecord(Fee fee, Double amount, String reference, String method) {
//        FeePayment payment = new FeePayment();
//        payment.setFeeId(fee.getId());
//        payment.setStudentId(fee.getStudentId());
//        payment.setAmount(amount);
//        payment.setPaymentDate(new Date());
//        payment.setPaymentReference(reference);
//        payment.setPaymentMethod(method != null ? method : "BANK_TRANSFER");
//        payment.setStatus("COMPLETED");
//
//        feePaymentRepository.save(payment);
//        log.info("Payment record created for fee ID: {}, amount: {}", fee.getFeeId(), amount);
//    }

    /**
     * Get complete fee information with payment history
     */
    public Fee getFeeWithPayments(String studentId, String academicYear, String semester) {
        log.info("Fetching complete fee information for student: {}, year: {}, semester: {}",
                studentId, academicYear, semester);

        Optional<Fee> feeOpt = feeRepository.findByStudentIdAndAcademicYearAndSemester(
                studentId, academicYear, semester);

        if (!feeOpt.isPresent()) {
            // If no fee record exists, create one with zero payments
            Fee fee = initializeFeeRecord(studentId, academicYear, semester);
            return feeRepository.save(fee);
        }

        Fee fee = feeOpt.get();

        // Get payment history
        List<FeePayment> payments = feePaymentRepository.findByFeeId(fee.getFeeId());
        fee.setPayments(payments);

        // Ensure all calculated fields are up to date
        updateFeeCalculations(fee);

        return fee;
    }

    /**
     * Initialize a new fee record with zero payments
     */
    private Fee initializeFeeRecord(String studentId, String academicYear, String semester) {
        // Get student's department for fee calculation
        Student studentDepartment = studentService.getStudentDepartmentByStudentId(studentId);
        if (studentDepartment == null) {
            throw new RuntimeException("No department found for student: " + studentId);
        }

        String departmentId = studentDepartment.getDepartmentId();
        double totalFeeAmount = calculateTotalFeeAmount(departmentId, academicYear);

        Fee fee = new Fee();
        fee.setStudentId(studentId);
        fee.setAcademicYear(academicYear);
        fee.setSemester(semester);
        fee.setTotalAmount(totalFeeAmount);
        fee.setPaidAmount(0.0);
        fee.setRemainingAmount(totalFeeAmount);
        fee.setStatus(Fee.FeeStatus.valueOf("UNPAID"));

        Date currentDate = new Date();
//        fee.setCreatedAt(currentDate);
//        fee.setUpdatedAt(currentDate);

        return fee;
    }

    /**
     * Update fee calculations based on current payments
     */
    private void updateFeeCalculations(Fee fee) {
        String departmentId = studentService.getStudentDepartmentByStudentId(fee.getStudentId()).getDepartmentId();
        double totalFeeAmount = calculateTotalFeeAmount(departmentId, fee.getAcademicYear());

        fee.setTotalAmount(totalFeeAmount);
        double currentPaidAmount = fee.getPaidAmount() != null ? fee.getPaidAmount() : 0.0;
        double remainingAmount = Math.max(totalFeeAmount - currentPaidAmount, 0);
        fee.setRemainingAmount(remainingAmount);

        // Update status
        if (remainingAmount <= 0) {
            fee.setStatus(Fee.FeeStatus.valueOf("FULLY_PAID"));
        } else if (currentPaidAmount > 0) {
            fee.setStatus(Fee.FeeStatus.valueOf("PARTIAL_PAYMENT"));
        } else {
            fee.setStatus(Fee.FeeStatus.valueOf("UNPAID"));
        }

        fee.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== FEE BALANCE CALCULATION ====================

    /**
     * Get comprehensive fee balance information for a student
     */
    public FeeBalanceResponse getFeeBalance(String studentId, String academicYear, String semester) {
        log.info("Fetching fee balance for student: {}, year: {}, semester: {}",
                studentId, academicYear, semester);

        // Validate input
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        if (academicYear == null || academicYear.trim().isEmpty()) {
            throw new IllegalArgumentException("Academic year cannot be null or empty");
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester cannot be null or empty");
        }

        // Get student information
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        // Get student's department
        Student studentDepartment = studentService.getStudentDepartmentByStudentId(studentId);
        if (studentDepartment == null) {
            throw new RuntimeException("No department found for student: " + studentId);
        }

        String departmentId = studentDepartment.getDepartmentId();
        if (departmentId == null || departmentId.trim().isEmpty()) {
            throw new RuntimeException("Department ID is null or empty for student: " + studentId);
        }

        // Calculate total amount the student should pay based on fee categories
        double totalFeeAmount = calculateTotalFeeAmount(departmentId, academicYear);

        // Calculate total amount paid by student
        double totalPaid = calculateTotalPaid(studentId, academicYear, semester);

        // Calculate amount owing
        double amountOwing = Math.max(totalFeeAmount - totalPaid, 0);

        // Get fee categories breakdown
        List<FeeCategory> feeCategories = feeCategoryService.getFeeCategoriesByDepartmentId(
                academicYear, departmentId);

        // Create and return response
        FeeBalanceResponse response = new FeeBalanceResponse();
        response.setStudentId(studentId);
        response.setStudentName(student.getName());
        response.setDepartmentId(departmentId);
        response.setAcademicYear(academicYear);
        response.setSemester(semester);
        response.setTotalFeeAmount(totalFeeAmount);
        response.setTotalPaid(totalPaid);
        response.setAmountOwing(amountOwing);
        response.setPaymentStatus(amountOwing <= 0 ? "FULLY_PAID" : "PARTIAL_PAYMENT");
        response.setFeeCategories(feeCategories); // Include fee breakdown

        log.info("Fee balance calculated - Total: {}, Paid: {}, Owing: {}, Categories: {}",
                totalFeeAmount, totalPaid, amountOwing, feeCategories.size());

        return response;
    }

    /**
     * Calculate total fee amount based on department fee categories
     */
    private double calculateTotalFeeAmount(String departmentId, String academicYear) {
        List<FeeCategory> feeCategories = feeCategoryService.getFeeCategoriesByDepartmentId(
                academicYear, departmentId);

        if (feeCategories == null || feeCategories.isEmpty()) {
            log.warn("No fee categories found for department: {} in academic year: {}",
                    departmentId, academicYear);
            return 0.0;
        }

        double totalAmount = feeCategories.stream()
                .filter(category -> category.isMandatory()) // Only include mandatory fees
                .mapToDouble(FeeCategory::getAmount)
                .sum();

        log.debug("Total fee amount for department {} in year {}: {} (from {} categories)",
                departmentId, academicYear, totalAmount, feeCategories.size());

        return totalAmount;
    }

    /**
     * Calculate total amount paid by student for specific academic year and semester
     */
    private double calculateTotalPaid(String studentId, String academicYear, String semester) {
        // Get fee record for the specific year and semester
        Optional<Fee> feeOpt = feeRepository.findByStudentIdAndAcademicYearAndSemester(
                studentId, academicYear, semester);

        if (feeOpt.isPresent()) {
            Double paidAmount = feeOpt.get().getPaidAmount();
            return paidAmount != null ? paidAmount : 0.0;
        }

        log.debug("No fee record found for student: {}, returning 0 as paid amount", studentId);
        return 0.0;
    }

    // ==================== LEGACY METHODS (Updated) ====================

    /**
     * @deprecated Use registerPayment instead
     */
    @Deprecated
    public Fee createFee(CreateFeeRequest request) {
        return registerPayment(
                request.getStudentId(),
                request.getAcademicYear(),
                request.getSemester(),
                request.getPaidAmount(),
                null,
                "MANUAL_ENTRY"
        );
    }

    /**
     * Calculate total amount paid by student across all records
     */
    public double calculateFeePaid(String studentId) {
        List<Fee> fees = feeRepository.findByStudentId(studentId);
        if (fees == null || fees.isEmpty()) {
            log.debug("No fee payments found for student: {}", studentId);
            return 0.0;
        }

        double totalPaid = fees.stream()
                .mapToDouble(fee -> fee.getPaidAmount() != null ? fee.getPaidAmount() : 0.0)
                .sum();

        log.debug("Total paid by student {}: {}", studentId, totalPaid);
        return totalPaid;
    }

    /**
     * Get amount owing by student for specific academic year
     */
    public double getAmountOwingByStudent(String studentId, String academicYear) {
        log.info("Calculating amount owing for student: {} for academic year: {}",
                studentId, academicYear);

        Student studentDepartment = studentService.getStudentDepartmentByStudentId(studentId);
        if (studentDepartment == null) {
            throw new RuntimeException("No department found for student: " + studentId);
        }

        String departmentId = studentDepartment.getDepartmentId();
        double totalFeeAmount = calculateTotalFeeAmount(departmentId, academicYear);
        double totalPaid = calculateFeePaid(studentId);
        double amountOwing = totalFeeAmount - totalPaid;

        return Math.max(amountOwing, 0);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Get payment history for a student
     */
    public List<Fee> getPaymentHistory(String studentId, String academicYear) {
        return feeRepository.findByStudentIdAndAcademicYear(studentId, academicYear);
    }

    /**
     * Validate payment input parameters
     */
    private void validatePaymentInput(String studentId, String academicYear,
                                      String semester, Double paidAmount) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        if (academicYear == null || academicYear.trim().isEmpty()) {
            throw new IllegalArgumentException("Academic Year cannot be null or empty");
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester cannot be null or empty");
        }
        if (paidAmount == null || paidAmount <= 0) {
            throw new IllegalArgumentException("Paid amount must be a positive number greater than 0");
        }
    }

    /**
     * @deprecated Use validatePaymentInput instead
     */
    @Deprecated
    public void validateCreateFeeRequest(CreateFeeRequest request) {
        validatePaymentInput(
                request.getStudentId(),
                request.getAcademicYear(),
                request.getSemester(),
                request.getPaidAmount()
        );
    }













//    public Fee createFee(CreateFeeRequest request) { // Fixed typo: crateFee -> createFee
//        validateCreateFeeRequest(request);
//
//        Fee fee = new Fee();
//        fee.setStudentId(request.getStudentId());
//        fee.setAcademicYear(request.getAcademicYear());
//        fee.setPaidAmount(request.getPaidAmount());
//        fee.setPaymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : new Date());
//        fee.setSemester(request.getSemester());
//
//        return feeRepository.save(fee);
//    }
//
//    public Fee getFeeBalance(String studentId, String academicYear, String semester) {
//        log.info("Fetching fee balance for student: {}, year: {}, semester: {}", studentId, academicYear, semester);
//
//        Student student = studentService.getStudentById(studentId);
//        if (student == null) {
//            throw new RuntimeException("Student not found with ID: " + studentId);
//        }
//
//        Student studentDepartment = studentService.getStudentDepartmentByStudentId(studentId); // Fixed variable name
//        if (studentDepartment == null) {
//            throw new RuntimeException("No department found for student: " + studentId);
//        }
//
//        // Fixed this line - student.getName() already returns the name
//        String studentName = student.getName();
//        if (studentName == null || studentName.isEmpty()) {
//            throw new RuntimeException("No name found for student: " + studentId);
//        }
//
//
//        double totalPaid = calculateFeePaid(studentId);
//        double amountOwing = getAmountOwingByStudent(studentId, academicYear);
//
//        // Create a response object with all the information
//        FeeBalanceResponse response = new FeeBalanceResponse();
//        response.setStudentId(studentId);
//        response.setStudentName(student.getName());
//        response.setAcademicYear(academicYear);
//        response.setSemester(semester);
//        response.setTotalPaid(totalPaid);
//        response.setAmountOwing(amountOwing);
//        response.setTotalFeeAmount(totalPaid + amountOwing);
//
//        return response;
//
//        // Validate semester
//        if (semester == null || semester.isEmpty()) {
//            throw new RuntimeException("Semester cannot be null or empty.");
//        }
//
//        // Fetch fee record
//        Optional<Fee> feeOptional = feeRepository.findByStudentIdAndAcademicYearAndSemester(studentId, academicYear, semester);
//        if (feeOptional.isPresent()) {
//            return feeOptional.get();
//        } else {
//            throw new RuntimeException("No fee record found for student: " + studentId + ", year: " + academicYear + ", semester: " + semester);
//        }
//    }
//
//    public double calculateFeePaid(String studentId) { // Fixed method name casing
//        List<Fee> fee = feeRepository.findByStudentId(studentId);
//        if (fee == null || fee.isEmpty()) { // Simplified null/empty check
//            throw new RuntimeException("No fee payments found for student: " + studentId);
//        }
//
//        double totalPaid = fee.stream()
//                .mapToDouble(Fee::getAmount)
//                .sum();
//        return totalPaid;
//    }
//
//    // Method to get amount owing by student
//    public double getAmountOwingByStudent(String studentId, String academicYear) { // Added academicYear parameter
//        log.info("Calculating amount owing for student: {} for academic year: {}", studentId, academicYear);
//
//        Student studentDepartment = studentService.getStudentDepartmentByStudentId(studentId);
//        if (studentDepartment == null) {
//            throw new RuntimeException("No department found for student: " + studentId);
//        }
//
//        // Get fee categories for the specific academic year and department
//        List<FeeCategory> feeCategories = feeCategoryService.getFeeCategoriesByDepartmentId(academicYear, studentDepartment.getDepartmentId());
//        if (feeCategories == null || feeCategories.isEmpty()) {
//            throw new RuntimeException("No fee categories found for student: " + studentId + " in academic year: " + academicYear);
//        }
//
//        // Calculate total fee amount from categories
//        double totalFeeAmount = feeCategories.stream()
//                .mapToDouble(FeeCategory::getAmount)
//                .sum();
//
//        // Calculate total amount paid by student
//        double totalPaid = calculateFeePaid(studentId);
//
//        // Calculate amount owing
//        double amountOwing = totalFeeAmount - totalPaid;
//        return Math.max(amountOwing, 0); // Ensure non-negative amount
//    }
//
//
//
//
//
//
////method to validate feeCreationd parameters
//    public void validateCreateFeeRequest(CreateFeeRequest request) {
//        if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
//            throw new IllegalArgumentException("Student ID cannot be null or empty");
//        }
//        if (request.getAcademicYear() == null || request.getAcademicYear().isEmpty()) {
//            throw new IllegalArgumentException("Academic Year cannot be null or empty");
//        }
//        if (request.getSemester() == null || request.getSemester().isEmpty()) {
//            throw new IllegalArgumentException("Semester cannot be null or empty");
//        }
//        if (request.getPaidAmount() == null || request.getPaidAmount() < 0) {
//            throw new IllegalArgumentException("Paid amount must be a positive number");
//        }
//    }
//
//
//
//
//
//








    /**
     * Generate fee record for student based on applicable fee categories
     * This is the recommended way to create fee records
     *
     */
    public Fee generateStudentFeeRecord(String studentId, String academicYear, String semester) {

        log.info("Generating fee record for student: {}, year: {}, semester: {}",
                studentId, academicYear, semester);

        // Validate student exists
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        // Check if fee record already exists
        Optional<Fee> existingFee = getFeeRecord(studentId, academicYear, semester);
        if (existingFee.isPresent()) {
            log.warn("Fee record already exists for student: {}", studentId);
            return existingFee.get();
        }

        // Get applicable fee categories for student's department
        List<FeeCategory> applicableCategories = feeCategoryService
                .getApplicableFeeCategories(student.getDepartmentId(), academicYear);

        if (applicableCategories.isEmpty()) {
            throw new RuntimeException("No fee categories found for department: " + student.getDepartmentId());
        }

        // Calculate total mandatory fees
        Double mandatoryFees = applicableCategories.stream()
                .filter(FeeCategory::isMandatory)
                .mapToDouble(FeeCategory::getAmount)
                .sum();

        // Create and save fee record
        Fee fee = Fee.builder()
                .feeId(UUID.randomUUID().toString())
                .studentId(studentId)
                .academicYear(academicYear)
                .semester(semester)
                .totalAmount(mandatoryFees)
                .paidAmount(0.0)
                .remainingAmount(mandatoryFees)
                .status(Fee.FeeStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Fee savedFee = feeRepository.save(fee);
        log.info("Fee record created successfully with ID: {}", savedFee.getFeeId());
        return savedFee;
    }

    /**
     * Create fee record with manual amount (for special cases)
     * Use generateStudentFeeRecord() instead when possible
     */


    public Fee createFeeRecord(String studentId, String academicYear, String semester, Double totalAmount) {
        log.info("Creating manual fee record for student: {}", studentId);

        // Validate inputs
        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than 0");
        }

        Fee fee = Fee.builder()
                .feeId(UUID.randomUUID().toString())
                .studentId(studentId)
                .academicYear(academicYear)
                .semester(semester)
                .totalAmount(totalAmount)
                .paidAmount(0.0)
                .remainingAmount(totalAmount)
                .status(Fee.FeeStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return feeRepository.save(fee);
    }

    // ==================== FEE RETRIEVAL ====================

    /**
     * Get all fee records for a student
     */
    public List<Fee> getStudentFees(String studentId) {
        log.debug("Retrieving fees for student: {}", studentId);
        return feeRepository.findByStudentId(studentId);
    }

    /**
     * Get specific fee record
     */
    public Optional<Fee> getFeeRecord(String studentId, String academicYear, String semester) {
        return feeRepository.findByStudentIdAndAcademicYearAndSemester(studentId, academicYear, semester);
    }

    /**
     * Get fee breakdown for a student (shows what categories they need to pay)
     */
    public List<FeeCategory> getStudentFeeBreakdown(String studentId, String academicYear) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        return feeCategoryService.getApplicableFeeCategories(student.getDepartmentId(), academicYear);
    }

    // ==================== PAYMENT PROCESSING ====================

    /**
     * Process fee payment
     */
    public FeePayment processPayment(String feeId, Double amount, FeePayment.PaymentMethod paymentMethod,
                                     String phoneNumber, String processedBy) {
        log.info("Processing payment for fee: {}, amount: {}, method: {}", feeId, amount, paymentMethod);

        // Validate input
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // Get fee record
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee record not found with ID: " + feeId));

        // Check if payment amount doesn't exceed remaining amount
        if (amount > fee.getRemainingAmount()) {
            throw new RuntimeException("Payment amount exceeds remaining fee amount");
        }

        // Create payment record
        FeePayment payment = FeePayment.builder()
                .paymentId(UUID.randomUUID().toString())
                .feeId(feeId)
                .studentId(fee.getStudentId())
                .amount(amount)
                .paymentMethod(paymentMethod)
                .phoneNumber(phoneNumber)
                .processedBy(processedBy)
                .createdAt(LocalDateTime.now())
                .status(FeePayment.PaymentStatus.PENDING)
                .build();

        // Process based on payment method
        try {
            switch (paymentMethod) {
                case MOMO:
                case ORANGE_MONEY:
                    String transactionId = mobileMoneyService.initiatePayment(phoneNumber, amount, paymentMethod);
                    payment.setTransactionId(transactionId);
                    break;

                case CASH:
                case BANK_TRANSFER:
                    // For cash/bank transfer, mark as completed immediately
                    payment.setStatus(FeePayment.PaymentStatus.COMPLETED);
                    payment.setPaymentDate(LocalDateTime.now());
                    updateFeeRecord(fee, amount);
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
            }
        } catch (Exception e) {
            log.error("Payment processing failed: {}", e.getMessage());
            payment.setStatus(FeePayment.PaymentStatus.FAILED);
        }

        FeePayment savedPayment = feePaymentRepository.save(payment);
        log.info("Payment record created with ID: {}", savedPayment.getPaymentId());
        return savedPayment;
    }

    /**
     * Confirm mobile money payment (called after external payment verification)
     */
    public void confirmMobileMoneyPayment(String paymentId, String bankReference) {
        log.info("Confirming mobile money payment: {}", paymentId);

        FeePayment payment = feePaymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment record not found with ID: " + paymentId));

        // Update payment status
        payment.setStatus(FeePayment.PaymentStatus.COMPLETED);
        payment.setBankReference(bankReference);
        payment.setPaymentDate(LocalDateTime.now());
        feePaymentRepository.save(payment);

        // Update corresponding fee record
        Fee fee = feeRepository.findById(payment.getFeeId())
                .orElseThrow(() -> new RuntimeException("Fee record not found"));

        updateFeeRecord(fee, payment.getAmount());
        log.info("Mobile money payment confirmed successfully");
    }

    /**
     * Update fee record after successful payment
     */
    private void updateFeeRecord(Fee fee, Double paidAmount) {
        log.debug("Updating fee record: {} with payment amount: {}", fee.getFeeId(), paidAmount);

        fee.setPaidAmount(fee.getPaidAmount() + paidAmount);
        fee.setRemainingAmount(fee.getTotalAmount() - fee.getPaidAmount());

        // Update status based on payment completion
        if (fee.getRemainingAmount() <= 0) {
            fee.setStatus(Fee.FeeStatus.PAID);
            log.info("Fee fully paid for student: {}", fee.getStudentId());
        } else if (fee.getPaidAmount() > 0) {
            fee.setStatus(Fee.FeeStatus.PARTIAL_PAYMENT);
        }

        fee.setUpdatedAt(LocalDateTime.now());
        feeRepository.save(fee);
    }

    // ==================== PAYMENT HISTORY ====================

    /**
     * Get payment history for a student
     */
    public List<FeePayment> getPaymentHistory(String studentId) {
        log.debug("Retrieving payment history for student: {}", studentId);
        return feePaymentRepository.findByStudentId(studentId);
    }

    /**
     * Get payments for a specific fee
     */
    public List<FeePayment> getFeePayments(String feeId) {
        return feePaymentRepository.findByFeeId(feeId);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Check if student has outstanding fees
     */
    public boolean hasOutstandingFees(String studentId) {
        List<Fee> fees = getStudentFees(studentId);
        return fees.stream().anyMatch(fee -> fee.getRemainingAmount() > 0);
    }

    /**
     * Get total outstanding amount for student
     */
    public Double getTotalOutstandingAmount(String studentId) {
        List<Fee> fees = getStudentFees(studentId);
        return fees.stream()
                .mapToDouble(Fee::getRemainingAmount)
                .sum();
    }
}