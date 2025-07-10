package com.example.resultmanagementsystem.Dto.complainDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryDTO {
    private String id;
    private String name;
    private String email;
    private String registrationNumber;
}