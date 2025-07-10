package com.example.resultmanagementsystem.Dto.complainDTO;

import com.example.resultmanagementsystem.model.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSummaryDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Role role;

    public TeacherSummaryDTO(String id, String s, String email, Role role) {
    }
}
