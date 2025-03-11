package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherDTO {
    private String id =UUID.randomUUID().toString();
    private String firstname;
    private String lastname;
    private String email;
    private Set departmentId;
    private Role role;
}
