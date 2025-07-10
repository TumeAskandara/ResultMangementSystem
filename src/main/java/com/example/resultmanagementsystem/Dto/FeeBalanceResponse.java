package com.example.resultmanagementsystem.Dto;
import com.example.resultmanagementsystem.model.FeeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class FeeBalanceResponse {

        @Id
        private String feeId;
        private String studentId;
        private String academicYear;
        private String semester;
        private Double totalPaid;
        private Double totalFeeAmount;
        private String studentName;
        private Double paidAmount;
        private Double amountOwing;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String departmentId;
        private String paymentStatus;
        private List<FeeCategory> feeCategories;



}
