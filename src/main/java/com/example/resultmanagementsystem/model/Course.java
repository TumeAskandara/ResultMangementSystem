package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
@Data
public class Course {
    @Id
    private String Id = UUID.randomUUID().toString();
    private String code;
    private String courseTitle;
    private String courseMaster;
    private double credits;
}
