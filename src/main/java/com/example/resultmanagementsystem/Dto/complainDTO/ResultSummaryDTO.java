package com.example.resultmanagementsystem.Dto.complainDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultSummaryDTO {
    private String id;
    private String studentId;
    private String courseId;
    private double marks;
    private String grade;
}