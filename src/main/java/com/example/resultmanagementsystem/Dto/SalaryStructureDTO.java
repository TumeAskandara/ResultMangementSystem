package com.example.resultmanagementsystem.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Salary Structure")
public class SalaryStructureDTO {

    @Schema(description = "Designation", example = "Senior Teacher")
    private String designation;

    @Schema(description = "Basic salary", example = "50000.00")
    private double basicSalary;

    @Schema(description = "Housing allowance", example = "10000.00")
    private double housingAllowance;

    @Schema(description = "Transport allowance", example = "5000.00")
    private double transportAllowance;

    @Schema(description = "Medical allowance", example = "3000.00")
    private double medicalAllowance;

    @Schema(description = "Tax deduction", example = "8000.00")
    private double taxDeduction;

    @Schema(description = "Pension deduction", example = "5000.00")
    private double pensionDeduction;

    @Schema(description = "Insurance deduction", example = "2000.00")
    private double insuranceDeduction;

    @Schema(description = "Effective from date", example = "2024-01-01")
    private LocalDate effectiveFrom;

    @Schema(description = "Whether the structure is active", example = "true")
    private boolean isActive;
}
