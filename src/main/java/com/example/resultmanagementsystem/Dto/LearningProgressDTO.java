package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.LearningProgress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LearningProgressDTO {
    private String courseId;
    private String moduleId;
    private LearningProgress.ProgressStatus status;
    private double progressPercentage;
    private int timeSpentMinutes;
}
