package com.example.resultmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
@Data
public class Course {
    @Id
    private String id = UUID.randomUUID().toString();
    private String code;
    private String courseTitle;
    private Set<String> departmentId;
    private String teacherId;
    private String courseMaster;
    private double credits;
    private String semester;

//    public String getName() {
//        return this.courseTitle;
//    }


}
