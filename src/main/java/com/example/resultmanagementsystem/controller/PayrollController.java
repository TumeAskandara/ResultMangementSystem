package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.PayrollGenerateDTO;
import com.example.resultmanagementsystem.Dto.PayslipDTO;
import com.example.resultmanagementsystem.Dto.SalaryStructureDTO;
import com.example.resultmanagementsystem.model.PayrollRecord;
import com.example.resultmanagementsystem.model.SalaryStructure;
import com.example.resultmanagementsystem.services.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Payroll Management", description = "APIs for managing payroll generation, salary structures, payment processing, and payslip generation.")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/generate")
    @Operation(
            summary = "Generate payroll for a staff member",
            description = "Generates a payroll record for a specific staff member for a given month and year based on their salary structure."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payroll generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PayrollRecord.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or payroll already exists for the period",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Staff member not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<PayrollRecord> generatePayroll(
            @Parameter(description = "Payroll generation details", required = true)
            @RequestBody PayrollGenerateDTO payrollGenerateDTO) {
        PayrollRecord payrollRecord = payrollService.generatePayroll(payrollGenerateDTO);
        return new ResponseEntity<>(payrollRecord, HttpStatus.CREATED);
    }

    @PostMapping("/generate/bulk")
    @Operation(
            summary = "Generate bulk payroll",
            description = "Generates payroll records for all active staff members for a given month and year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bulk payroll generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Invalid month or year",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<PayrollRecord>> generateBulkPayroll(
            @Parameter(description = "Month (1-12)", required = true, example = "3")
            @RequestParam int month,
            @Parameter(description = "Year", required = true, example = "2024")
            @RequestParam int year) {
        List<PayrollRecord> records = payrollService.generateBulkPayroll(month, year);
        return new ResponseEntity<>(records, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get payroll record by ID",
            description = "Retrieves a specific payroll record using its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payroll record retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PayrollRecord.class))),
            @ApiResponse(responseCode = "404", description = "Payroll record not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<PayrollRecord> getPayrollById(
            @Parameter(description = "Payroll record ID", required = true)
            @PathVariable String id) {
        PayrollRecord payrollRecord = payrollService.getPayrollById(id);
        return new ResponseEntity<>(payrollRecord, HttpStatus.OK);
    }

    @GetMapping("/staff/{staffId}")
    @Operation(
            summary = "Get payroll records by staff member",
            description = "Retrieves all payroll records for a specific staff member."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payroll records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<PayrollRecord>> getPayrollByStaff(
            @Parameter(description = "Staff member ID", required = true)
            @PathVariable String staffId) {
        List<PayrollRecord> records = payrollService.getPayrollByStaff(staffId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/monthly")
    @Operation(
            summary = "Get payroll records by month (paginated)",
            description = "Retrieves a paginated list of payroll records for a specific month and year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly payroll records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<PayrollRecord>> getPayrollByMonth(
            @Parameter(description = "Month (1-12)", required = true, example = "3")
            @RequestParam int month,
            @Parameter(description = "Year", required = true, example = "2024")
            @RequestParam int year,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PayrollRecord> records = payrollService.getPayrollByMonth(month, year, pageable);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PatchMapping("/{id}/process")
    @Operation(
            summary = "Process payroll payment",
            description = "Marks a pending payroll record as processed and records the payment date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PayrollRecord.class))),
            @ApiResponse(responseCode = "400", description = "Payroll is not in PENDING status",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Payroll record not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<PayrollRecord> processPayment(
            @Parameter(description = "Payroll record ID", required = true)
            @PathVariable String id) {
        PayrollRecord processedRecord = payrollService.processPayment(id);
        return new ResponseEntity<>(processedRecord, HttpStatus.OK);
    }

    @GetMapping("/{id}/payslip")
    @Operation(
            summary = "Get payslip",
            description = "Generates and retrieves a payslip for a specific payroll record."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payslip generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PayslipDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payroll record not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<PayslipDTO> getPayslip(
            @Parameter(description = "Payroll record ID", required = true)
            @PathVariable String id) {
        PayslipDTO payslip = payrollService.getPayslip(id);
        return new ResponseEntity<>(payslip, HttpStatus.OK);
    }

    @PostMapping("/salary-structure")
    @Operation(
            summary = "Create a salary structure",
            description = "Creates a new salary structure for a specific designation with defined allowances and deductions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Salary structure created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SalaryStructure.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<SalaryStructure> createSalaryStructure(
            @Parameter(description = "Salary structure details", required = true)
            @RequestBody SalaryStructureDTO salaryStructureDTO) {
        SalaryStructure salaryStructure = payrollService.createSalaryStructure(salaryStructureDTO);
        return new ResponseEntity<>(salaryStructure, HttpStatus.CREATED);
    }

    @GetMapping("/salary-structure")
    @Operation(
            summary = "Get all active salary structures",
            description = "Retrieves all active salary structures defined in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salary structures retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<SalaryStructure>> getSalaryStructures() {
        List<SalaryStructure> structures = payrollService.getSalaryStructures();
        return new ResponseEntity<>(structures, HttpStatus.OK);
    }

    @GetMapping("/salary-structure/{designation}")
    @Operation(
            summary = "Get salary structure by designation",
            description = "Retrieves the salary structure for a specific designation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salary structure retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SalaryStructure.class))),
            @ApiResponse(responseCode = "404", description = "Salary structure not found for designation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<SalaryStructure> getSalaryStructureByDesignation(
            @Parameter(description = "Designation name", required = true, example = "Senior Teacher")
            @PathVariable String designation) {
        SalaryStructure structure = payrollService.getSalaryStructureByDesignation(designation);
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
