package com.example.resultmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "departments")
public class Department {
    @Id

    private String departmentId = UUID.randomUUID().toString();
    private String departmentName;

}
