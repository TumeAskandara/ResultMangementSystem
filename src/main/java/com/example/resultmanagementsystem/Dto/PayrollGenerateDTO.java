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
@Schema(description = "Data transfer object for generating payroll")
public class PayrollGenerateDTO {

    @Schema(description = "Staff ID", example = "STAFF001")
    private String staffId;

    @Schema(description = "Payroll month (1-12)", example = "3")
    private int month;

    @Schema(description = "Payroll year", example = "2024")
    private int year;
}
