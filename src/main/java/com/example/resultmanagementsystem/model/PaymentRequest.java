package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public
class PaymentRequest {
    private String feeId;
    private Double amount;
    private FeePayment.PaymentMethod paymentMethod;
    private String phoneNumber;
    private String processedBy;
}
