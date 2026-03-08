package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "hostel_allocations")
public class HostelAllocation {
    @Id
    private String id = UUID.randomUUID().toString();
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
    private AllocationStatus status;
    private double hostelFee;
    private PaymentStatus paymentStatus;

    public enum AllocationStatus {
        ACTIVE, VACATED, TRANSFERRED
    }

    public enum PaymentStatus {
        PAID, PENDING, PARTIAL
    }
}
