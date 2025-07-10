package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "fee_categories")
public class FeeCategory {
    @Id
    private String categoryId;
    private String name;
    private String description;
    private Double amount;
    private String departmentId;
    private String academicYear;
    private boolean mandatory;
}