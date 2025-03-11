package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultDTO {
    private String studentId;
    private String courseId;
    private Double ca;
    private Double exams;
    private Double credit;
}
