package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.HostelAllocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelAllocationDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String hostelId;
    private String hostelName;
    private String roomId;
    private String roomNumber;
    private int bedNumber;
    private String academicYear;
    private String semester;
    private LocalDate allocationDate;
    private LocalDate vacatingDate;
    private HostelAllocation.AllocationStatus status;
    private double hostelFee;
    private HostelAllocation.PaymentStatus paymentStatus;
}
