package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.GradeScaleEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeScaleDTO {
    private String name;
    private String departmentId;
    private List<GradeScaleEntry> entries;
    private boolean isDefault;
}
