package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.TransportAssignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportAssignmentDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String routeId;
    private String routeName;
    private String stopName;
    private String academicYear;
    private TransportAssignment.TransportAssignmentStatus status;
    private double transportFee;
}
