package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.GradeAppeal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeAppealReviewDTO {
    private GradeAppeal.AppealStatus status;
    private String reviewedBy;
    private String reviewNotes;
}
