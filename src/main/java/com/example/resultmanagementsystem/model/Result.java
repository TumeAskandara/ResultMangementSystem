package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "results")
public class Result {
    @Id
    private String id;
    private String studentId;
    private String courseId;
    private String year = Year.now().toString();
    private String status;
    private String semester;
    private double exams;
    private double ca;
    private double total;
    private String grade;
    private String evaluation;
    private double weight;
    private double credits;
    private String juryDecision;
    private double gpa;
    private double marks;
    private String courseMaster;


    public void setGpa(double gpa) {
        this.gpa = BigDecimal.valueOf(gpa)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }


}
