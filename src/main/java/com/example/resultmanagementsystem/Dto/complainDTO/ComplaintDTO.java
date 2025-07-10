package com.example.resultmanagementsystem.Dto.complainDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDTO {
    private String studentId;
    private String courseId;
    private String resultId; // Optional, may be null if not related to a specific result
    private String description;
}