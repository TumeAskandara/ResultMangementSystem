package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeCategoryDTO {
    private String departmentId;
    private String academicYear;
    private String categoryName;
    private double weight;
    private String courseId;
    private String createdBy;
}
