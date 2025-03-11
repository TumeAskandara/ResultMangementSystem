package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "results")
public class Result {
    @Id
    private String id;
    private String studentId;
    private String courseId;
    private String status;
    private double exams;
    private double ca;
    private double total;
    private String grade;
    private String evaluation;
    private double weight;
    private double credits;
    private String juryDecision;
    private Integer gpa;

    public double getExams() {
        return exams;
    }

    public void setExams(double exams) {
        this.exams = exams;
    }

}
