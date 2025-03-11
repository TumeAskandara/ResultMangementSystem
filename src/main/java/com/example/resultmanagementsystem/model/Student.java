package com.example.resultmanagementsystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection  = "students")
public class Student {
    @Id
    @JsonIgnore
    private String studentId = UUID.randomUUID().toString();
    private String name;
    private String departmentId;
    private String email;

}


