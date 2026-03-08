package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnrollmentDTO {
    private String studentId;
    private String admissionApplicationId;
    private String departmentId;
    private String academicYear;
    private String semester;
    private String enrollmentType;
    private String classId;
    private String section;
}
