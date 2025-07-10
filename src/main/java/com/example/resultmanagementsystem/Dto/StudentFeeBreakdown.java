//package com.example.resultmanagementsystem.Dto;
//
//import com.example.resultmanagementsystem.services.FeeService;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * Complete student fee breakdown with category details
// */
//@lombok.Data
//@lombok.Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public static class StudentFeeBreakdown {
//    private String studentId;
//    private String studentName;
//    private String departmentId;
//    private String academicYear;
//    private String semester;
//    private Double totalExpectedAmount;
//    private Double totalPaidAmount;
//    private Double totalRemainingAmount;
//    private String paymentStatus;
//    private List<FeeService.FeeCategoryBreakdown> categoryBreakdowns;
//    private LocalDateTime lastUpdated;
//}