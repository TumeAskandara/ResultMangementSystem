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
public class AuditLogDTO {
    private String id;
    private AuditLog.AuditAction action;
    private String entityType;
    private String entityId;
    private String performedBy;
    private String performedByRole;
    private String performedByEmail;
    private String details;
    private String previousValue;
    private String newValue;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
}
