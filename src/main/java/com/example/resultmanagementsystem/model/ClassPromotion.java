package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "class_promotions")
public class ClassPromotion {
    @Id
    private String id = UUID.randomUUID().toString();

    private String studentId;
    private String studentName;
    private String fromClassId;
    private String fromClassName;
    private String toClassId;
    private String toClassName;
    private String fromAcademicYear;
    private String toAcademicYear;
    private PromotionType promotionType;
    private String promotedBy;
    private String remarks;
    private LocalDateTime promotionDate;

    public enum PromotionType {
        PROMOTED, REPEATED, GRADUATED, TRANSFERRED, DROPPED_OUT
    }
}
