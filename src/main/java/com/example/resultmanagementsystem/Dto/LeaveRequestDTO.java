package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.LeaveRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Leave Request")
public class LeaveRequestDTO {

    @Schema(description = "Type of leave", example = "SICK")
    private LeaveRequest.LeaveType leaveType;

    @Schema(description = "Start date of leave", example = "2024-03-01")
    private LocalDate startDate;

    @Schema(description = "End date of leave", example = "2024-03-05")
    private LocalDate endDate;

    @Schema(description = "Reason for leave", example = "Medical appointment")
    private String reason;

    @Schema(description = "Attachment URL for supporting documents")
    private String attachmentUrl;
}
