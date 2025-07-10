package com.example.resultmanagementsystem.Dto.userdto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String message;
    private String lastName;
    private String email;
    private String role;
    private String firstName;
    private Set<String> departmentId;
    private String studentId;

}