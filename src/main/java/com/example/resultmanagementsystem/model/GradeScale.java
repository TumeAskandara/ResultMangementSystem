package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "grade_scales")
public class GradeScale {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String departmentId;
    private List<GradeScaleEntry> entries;
    private boolean isDefault;
    private LocalDateTime createdAt;
}
