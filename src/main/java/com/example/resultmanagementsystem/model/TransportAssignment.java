package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "transport_assignments")
public class TransportAssignment {
    @Id
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private String studentName;
    private String routeId;
    private String routeName;
    private String stopName;
    private String academicYear;
    private TransportAssignmentStatus status;
    private LocalDateTime assignedAt;
    private double transportFee;

    public enum TransportAssignmentStatus {
        ACTIVE, INACTIVE
    }
}
