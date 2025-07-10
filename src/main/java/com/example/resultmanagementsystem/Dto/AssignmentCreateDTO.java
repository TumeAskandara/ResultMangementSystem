// AssignmentCreateDTO.java
package com.example.resultmanagementsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssignmentCreateDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @Positive(message = "Max points must be positive")
    private Integer maxPoints;

    private List<String> attachmentUrls;
}