package com.example.resultmanagementsystem.Dto.complainDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSummaryDTO {
    private String id;
    private String code;
    private String name;
    private String teacherId;
}