package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.LeaveRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for reviewing a leave request")
public class LeaveReviewDTO {

    @Schema(description = "Review status", example = "APPROVED")
    private LeaveRequest.LeaveStatus status;

    @Schema(description = "Remarks from the approver", example = "Approved. Please ensure handover is complete.")
    private String approverRemarks;
}
