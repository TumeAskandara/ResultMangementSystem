package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.CreateFeeRequest;
import com.example.resultmanagementsystem.Dto.FeeBalanceResponse;
import com.example.resultmanagementsystem.model.*;
import com.example.resultmanagementsystem.services.FeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Fee Management", description = "APIs for managing student fees, payments, and fee-related operations")
public class FeeController {

    private final FeeService feeService;

    // ==================== FEE BALANCE AND INQUIRY ====================

    @Operation(
            summary = "Get student fee balance",
            description = "Retrieves the current fee balance for a specific student in a given academic year and semester. " +
                    "This includes total fees, paid amounts, and outstanding balance."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee balance retrieved successfully",
                    content = @Content(schema = @Schema(implementation = FeeBalanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Student or fee record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/balance")
    public ResponseEntity<FeeBalanceResponse> getFeeBalance(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @RequestParam String studentId,
            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
            @RequestParam String academicYear,
            @Parameter(description = "Semester (FIRST, SECOND, etc.)", required = true, example = "FIRST")
            @RequestParam String semester) {

        log.info("Fetching fee balance for student: {}, year: {}, semester: {}", studentId, academicYear, semester);
        FeeBalanceResponse balance = feeService.getFeeBalance(studentId, academicYear, semester);
        return ResponseEntity.ok(balance);
    }

    @Operation(
            summary = "Get student payment history",
            description = "Retrieves the complete payment history for a student in a specific academic year. " +
                    "Returns all fee records and associated payments made by the student."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment history retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/payment-history")
    public ResponseEntity<List<Fee>> getPaymentHistory(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @RequestParam String studentId,
            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
            @RequestParam String academicYear) {

        log.info("Fetching payment history for student: {}, year: {}", studentId, academicYear);
        List<Fee> payments = feeService.getPaymentHistory(studentId, academicYear);
        return ResponseEntity.ok(payments);
    }

    // ==================== FEE RECORD CREATION ====================

    @Operation(
            summary = "Create new fee record",
            description = "Creates a new fee record for a student with specified details. " +
                    "This is used for manual fee record creation with custom amounts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fee record created successfully",
                    content = @Content(schema = @Schema(implementation = Fee.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed"),
            @ApiResponse(responseCode = "409", description = "Fee record already exists for this period"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<Fee> createFee(
            @Parameter(description = "Fee creation request details", required = true)
            @Valid @RequestBody CreateFeeRequest request) {

        log.info("Creating fee record for student: {}", request.getStudentId());
        Fee createdFee = feeService.createFee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFee);
    }

    @Operation(
            summary = "Generate automatic fee record",
            description = "Automatically generates a fee record for a student based on their applicable fee categories. " +
                    "This is the recommended way to create fee records as it applies all relevant fees automatically."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee record generated successfully",
                    content = @Content(schema = @Schema(implementation = Fee.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters or fee generation failed"),
            @ApiResponse(responseCode = "404", description = "Student not found or no applicable fee categories"),
            @ApiResponse(responseCode = "409", description = "Fee record already exists for this period"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/generate")
    public ResponseEntity<Fee> generateStudentFeeRecord(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @RequestParam String studentId,
            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
            @RequestParam String academicYear,
            @Parameter(description = "Semester (FIRST, SECOND, etc.)", required = true, example = "FIRST")
            @RequestParam String semester) {

        log.info("Generating fee record for student: {}, year: {}, semester: {}", studentId, academicYear, semester);
        Fee fee = feeService.generateStudentFeeRecord(studentId, academicYear, semester);
        return ResponseEntity.ok(fee);
    }

    // ==================== FEE RETRIEVAL ====================

    @Operation(
            summary = "Get all fees for a student",
            description = "Retrieves all fee records for a specific student across all academic years and semesters. " +
                    "Automatically generates missing fee records if they don't exist."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student fees retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Fee>> getStudentFees(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId) {

        log.debug("Retrieving fees for student: {}", studentId);
        List<Fee> fees = feeService.getStudentFees(studentId);
        return ResponseEntity.ok(fees);
    }

    @Operation(
            summary = "Get specific fee record",
            description = "Retrieves a specific fee record for a student in a particular academic year and semester."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee record found and retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Fee.class))),
            @ApiResponse(responseCode = "404", description = "Fee record not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/record/student/{studentId}/year/{academicYear}/semester/{semester}")
    public ResponseEntity<Fee> getFeeRecord(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId,
            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
            @PathVariable String academicYear,
            @Parameter(description = "Semester (FIRST, SECOND, etc.)", required = true, example = "FIRST")
            @PathVariable String semester) {

        log.debug("Retrieving fee record for student: {}, year: {}, semester: {}", studentId, academicYear, semester);
        Optional<Fee> fee = feeService.getFeeRecord(studentId, academicYear, semester);
        return fee.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get student fee breakdown",
            description = "Retrieves detailed breakdown of fee categories applicable to a student for a specific academic year. " +
                    "Shows individual fee categories and their amounts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee breakdown retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Student not found or no fee categories applicable"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/breakdown/student/{studentId}/year/{academicYear}")
    public ResponseEntity<List<FeeCategory>> getStudentFeeBreakdown(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId,
            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
            @PathVariable String academicYear) {

        log.debug("Retrieving fee breakdown for student: {}, year: {}", studentId, academicYear);
        List<FeeCategory> breakdown = feeService.getStudentFeeBreakdown(studentId, academicYear);
        return ResponseEntity.ok(breakdown);
    }

    // ==================== PAYMENT PROCESSING ====================

    @Operation(
            summary = "Process fee payment",
            description = "Processes a payment for a specific fee record. Supports various payment methods including " +
                    "mobile money, bank transfer, and cash payments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully",
                    content = @Content(schema = @Schema(implementation = FeePayment.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment request or insufficient data"),
            @ApiResponse(responseCode = "404", description = "Fee record not found"),
            @ApiResponse(responseCode = "409", description = "Payment amount exceeds outstanding balance"),
            @ApiResponse(responseCode = "500", description = "Payment processing failed")
    })
    @PostMapping("/payment")
    public ResponseEntity<FeePayment> processPayment(
            @Parameter(description = "Payment processing request details", required = true)
            @Valid @RequestBody PaymentRequest request) {

        log.info("Processing payment for fee: {}, amount: {}", request.getFeeId(), request.getAmount());
        FeePayment payment = feeService.processPayment(
                request.getFeeId(),
                request.getAmount(),
                request.getPaymentMethod(),
                request.getPhoneNumber(),
                request.getProcessedBy()
        );
        return ResponseEntity.ok(payment);
    }

    @Operation(
            summary = "Confirm mobile money payment",
            description = "Confirms a mobile money payment using bank reference number. " +
                    "This updates the payment status from PENDING to CONFIRMED."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment ID or confirmation request"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "409", description = "Payment already confirmed or cancelled"),
            @ApiResponse(responseCode = "500", description = "Payment confirmation failed")
    })
    @PostMapping("/payment/{paymentId}/confirm")
    public ResponseEntity<Map<String, String>> confirmPayment(
            @Parameter(description = "Unique identifier of the payment", required = true, example = "PAY001")
            @PathVariable String paymentId,
            @Parameter(description = "Payment confirmation details", required = true)
            @RequestBody ConfirmPaymentRequest request) {

        log.info("Confirming payment: {}", paymentId);
        feeService.confirmMobileMoneyPayment(paymentId, request.getBankReference());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment confirmed successfully");
        response.put("paymentId", paymentId);
        return ResponseEntity.ok(response);
    }

    // ==================== PAYMENT HISTORY ====================

    @Operation(
            summary = "Get student payment history",
            description = "Retrieves complete payment history for a student across all academic years. " +
                    "Includes all payment transactions with details like amount, method, and status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment history retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/payments/student/{studentId}")
    public ResponseEntity<List<FeePayment>> getStudentPaymentHistory(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId) {

        log.debug("Retrieving payment history for student: {}", studentId);
        List<FeePayment> payments = feeService.getPaymentHistory(studentId);
        return ResponseEntity.ok(payments);
    }

    @Operation(
            summary = "Get payments for specific fee",
            description = "Retrieves all payments made towards a specific fee record. " +
                    "Useful for tracking partial payments and payment history for a single fee."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee payments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid fee ID"),
            @ApiResponse(responseCode = "404", description = "Fee record not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/payments/fee/{feeId}")
    public ResponseEntity<List<FeePayment>> getFeePayments(
            @Parameter(description = "Unique identifier of the fee record", required = true, example = "FEE001")
            @PathVariable String feeId) {

        log.debug("Retrieving payments for fee: {}", feeId);
        List<FeePayment> payments = feeService.getFeePayments(feeId);
        return ResponseEntity.ok(payments);
    }

    // ==================== FEE STATUS AND ANALYSIS ====================

    @Operation(
            summary = "Check outstanding fees status",
            description = "Checks if a student has any outstanding fees for the current academic year. " +
                    "Returns both status and total outstanding amount."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outstanding fees status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/student/{studentId}/outstanding")
    public ResponseEntity<Map<String, Object>> hasOutstandingFees(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId) {

        log.debug("Checking outstanding fees for student: {}", studentId);
        boolean hasOutstanding = feeService.hasOutstandingFees(studentId);
        Double totalOutstanding = feeService.getTotalOutstandingAmount(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("hasOutstandingFees", hasOutstanding);
        response.put("totalOutstandingAmount", totalOutstanding);

        return ResponseEntity.ok(response);
    }

    // ==================== ADDITIONAL UTILITY ENDPOINTS ====================

    @Operation(
            summary = "Get fee summary for student",
            description = "Provides a comprehensive summary of a student's fee status including total records, " +
                    "outstanding amounts, and payment status across all academic periods."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fee summary retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/summary/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentFeeSummary(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId) {

        log.debug("Retrieving fee summary for student: {}", studentId);
        List<Fee> allFees = feeService.getStudentFees(studentId);
        boolean hasOutstanding = feeService.hasOutstandingFees(studentId);
        Double totalOutstanding = feeService.getTotalOutstandingAmount(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("totalFeeRecords", allFees.size());
        response.put("hasOutstandingFees", hasOutstanding);
        response.put("totalOutstandingAmount", totalOutstanding);
        response.put("feeRecords", allFees);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get student owing amount",
            description = "Calculates and returns the total amount owed by a student for a specific academic year. " +
                    "This includes all unpaid fees and partial payments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Owing amount calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })


//    @GetMapping("/owing/student/{studentId}/year/{academicYear}")
//    public ResponseEntity<Map<String, Object>> getStudentOwingAmount(
//            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
//            @PathVariable String studentId,
//            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
//            @PathVariable String academicYear) {
//
//        log.debug("Calculating owing amount for student: {}, year: {}", studentId, academicYear);
//        Double owingAmount = feeService.getStudentOwingAmount(studentId, academicYear);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("studentId", studentId);
//        response.put("academicYear", academicYear);
//        response.put("owingAmount", owingAmount);
//        response.put("hasOutstandingFees", owingAmount > 0);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @Operation(
//            summary = "Get total paid by student",
//            description = "Calculates and returns the total amount paid by a student for a specific academic year. " +
//                    "Includes all confirmed payments across all fee categories."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Total paid amount calculated successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
//            @ApiResponse(responseCode = "404", description = "Student not found"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//
//

//    @GetMapping("/paid/student/{studentId}/year/{academicYear}")
//    public ResponseEntity<Map<String, Object>> getTotalPaidByStudent(
//            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
//            @PathVariable String studentId,
//            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
//            @PathVariable String academicYear) {
//
//        log.debug("Calculating total paid for student: {}, year: {}", studentId, academicYear);
//        Double totalPaid = feeService.getTotalPaidByStudent(studentId, academicYear);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("studentId", studentId);
//        response.put("academicYear", academicYear);
//        response.put("totalPaidAmount", totalPaid);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @Operation(
//            summary = "Check mandatory fees payment status",
//            description = "Verifies if a student has paid all mandatory fees for a specific academic year. " +
//                    "Returns boolean status indicating compliance with mandatory fee requirements."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Mandatory fees status checked successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
//            @ApiResponse(responseCode = "404", description = "Student not found"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })


    @GetMapping("/status/student/{studentId}/year/{academicYear}/mandatory-paid")
    public ResponseEntity<Map<String, Object>> hasPaidMandatoryFees(
            @Parameter(description = "Unique identifier of the student", required = true, example = "STU001")
            @PathVariable String studentId,
            @Parameter(description = "Academic year in format YYYY-YYYY", required = true, example = "2024-2025")
            @PathVariable String academicYear) {

        log.debug("Checking mandatory fees payment status for student: {}, year: {}", studentId, academicYear);
//        boolean hasPaidMandatory = feeService.hasStudentPaidMandatoryFees(studentId, academicYear);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("academicYear", academicYear);
//        response.put("hasPaidMandatoryFees", hasPaidMandatory);

        return ResponseEntity.ok(response);
    }
}