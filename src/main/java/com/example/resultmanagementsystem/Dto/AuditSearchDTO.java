package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.AuditLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuditSearchDTO {
    private String entityType;
    private String entityId;
    private String performedBy;
    private AuditLog.AuditAction action;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
