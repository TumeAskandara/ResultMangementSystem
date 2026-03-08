package com.example.resultmanagementsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TranscriptRequestDTO {
    @NotBlank(message = "Student ID is required")
    private String studentId;
}
