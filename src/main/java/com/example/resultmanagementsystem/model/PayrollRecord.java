package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payroll_records")
public class PayrollRecord {

    @Id
    private String id = UUID.randomUUID().toString();

    private String staffId;
    private String staffName;
    private int month;
    private int year;
    private double basicSalary;
    private Map<String, Double> allowances;
    private Map<String, Double> deductions;
    private double totalAllowances;
    private double totalDeductions;
    private double netSalary;
    private double grossSalary;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String bankReference;
    private String generatedBy;
    private LocalDateTime generatedAt;
    private String remarks;

    public enum PaymentStatus {
        PENDING, PROCESSED, PAID, CANCELLED
    }
}
