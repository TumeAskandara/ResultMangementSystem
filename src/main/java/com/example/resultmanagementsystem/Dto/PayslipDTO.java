package com.example.resultmanagementsystem.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Payslip")
public class PayslipDTO {

    @Schema(description = "Payroll record ID")
    private String payrollId;

    @Schema(description = "Staff ID")
    private String staffId;

    @Schema(description = "Staff name")
    private String staffName;

    @Schema(description = "Pay month")
    private int month;

    @Schema(description = "Pay year")
    private int year;

    @Schema(description = "Basic salary")
    private double basicSalary;

    @Schema(description = "Allowances breakdown")
    private Map<String, Double> allowances;

    @Schema(description = "Deductions breakdown")
    private Map<String, Double> deductions;

    @Schema(description = "Total allowances")
    private double totalAllowances;

    @Schema(description = "Total deductions")
    private double totalDeductions;

    @Schema(description = "Gross salary")
    private double grossSalary;

    @Schema(description = "Net salary")
    private double netSalary;

    @Schema(description = "Payment status")
    private String paymentStatus;

    @Schema(description = "Payment date")
    private LocalDateTime paymentDate;

    @Schema(description = "Payment method")
    private String paymentMethod;

    @Schema(description = "Bank reference")
    private String bankReference;
}
