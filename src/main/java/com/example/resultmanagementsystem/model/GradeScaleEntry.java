package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeScaleEntry {
    private String grade;
    private double minScore;
    private double maxScore;
    private double gradePoint;
    private String description;
}
