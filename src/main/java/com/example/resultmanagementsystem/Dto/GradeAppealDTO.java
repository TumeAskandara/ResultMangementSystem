package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeAppealDTO {
    private String studentId;
    private String courseId;
    private String resultId;
    private String reason;
    private String currentGrade;
    private String requestedAction;
}
