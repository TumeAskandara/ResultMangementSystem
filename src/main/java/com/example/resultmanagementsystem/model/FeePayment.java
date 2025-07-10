package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "fee_payments")
public class FeePayment {
    @Id
    private String paymentId;
    private String feeId;
    private String studentId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private String phoneNumber;
    private String bankReference;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private String processedBy; // accountant ID

    public enum PaymentMethod {
        MOMO, ORANGE_MONEY, CASH, BANK_TRANSFER
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, CANCELLED
    }
}