package com.example.resultmanagementsystem.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Leave Balance")
public class LeaveBalanceDTO {

    @Schema(description = "Staff ID", example = "STAFF001")
    private String staffId;

    @Schema(description = "Academic year", example = "2024-2025")
    private String academicYear;

    @Schema(description = "Total sick leave days", example = "12")
    private int sickLeave;

    @Schema(description = "Total casual leave days", example = "10")
    private int casualLeave;

    @Schema(description = "Total annual leave days", example = "20")
    private int annualLeave;

    @Schema(description = "Total maternity leave days", example = "90")
    private int maternityLeave;

    @Schema(description = "Total paternity leave days", example = "14")
    private int paternityLeave;
}
