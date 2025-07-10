package com.example.resultmanagementsystem.Dto.complainDTO;

import com.example.resultmanagementsystem.model.Complaint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDetailDTO {
    private Complaint complaint;
    private StudentSummaryDTO student;
    private CourseSummaryDTO course;
    private ResultSummaryDTO result;
    private TeacherSummaryDTO assignedTeacher;
}
