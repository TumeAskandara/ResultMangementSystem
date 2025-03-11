package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private String studentId = UUID.randomUUID().toString();
    private String name;
    private String departmentId;
    private String role;
}
