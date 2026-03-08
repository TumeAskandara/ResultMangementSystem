package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "leave_balances")
public class LeaveBalance {

    @Id
    private String id = UUID.randomUUID().toString();

    private String staffId;
    private String academicYear;
    private int sickLeave;
    private int casualLeave;
    private int annualLeave;
    private int maternityLeave;
    private int paternityLeave;
    private int usedSickLeave;
    private int usedCasualLeave;
    private int usedAnnualLeave;
    private int usedMaternityLeave;
    private int usedPaternityLeave;
}
