package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "fees")
public class Fee {
    @Id
    private String feeId;
    private String studentId;
    private String academicYear;
    private String semester;
    private Double amount;
    private Double totalAmount;
    private Double paidAmount;
    private Double remainingAmount;
    private FeeStatus status;
    private Date paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FeePayment> payments = new ArrayList<>();



    public enum FeeStatus {
        PENDING, PARTIAL_PAYMENT, PAID, OVERDUE
    }
}