package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "salary_structures")
public class SalaryStructure {

    @Id
    private String id = UUID.randomUUID().toString();

    private String designation;
    private double basicSalary;
    private double housingAllowance;
    private double transportAllowance;
    private double medicalAllowance;
    private double taxDeduction;
    private double pensionDeduction;
    private double insuranceDeduction;
    private LocalDate effectiveFrom;
    private boolean isActive;
    private LocalDateTime createdAt;
}
