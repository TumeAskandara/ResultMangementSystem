package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.ClassPromotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClassPromotionDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String fromClassId;
    private String fromClassName;
    private String toClassId;
    private String toClassName;
    private String fromAcademicYear;
    private String toAcademicYear;
    private ClassPromotion.PromotionType promotionType;
    private String promotedBy;
    private String remarks;
    private LocalDateTime promotionDate;
}
