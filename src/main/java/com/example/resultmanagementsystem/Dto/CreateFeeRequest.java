package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public
class CreateFeeRequest {
    private String studentId;
    private String academicYear;
    private String semester;
    private Double paidAmount;
    private Date paymentDate;


}
