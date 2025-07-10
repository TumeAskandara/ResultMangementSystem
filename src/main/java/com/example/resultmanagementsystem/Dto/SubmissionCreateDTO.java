package com.example.resultmanagementsystem.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SubmissionCreateDTO {
    @NotNull(message = "Assignment ID is required")
    private String assignmentId;

    @NotNull(message = "At least one file is required")
    private List<String> fileUrls;

    private String comments;
}