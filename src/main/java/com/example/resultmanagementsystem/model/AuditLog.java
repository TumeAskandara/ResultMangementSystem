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
@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    private String id = UUID.randomUUID().toString();

    private AuditAction action;
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

    public enum AuditAction {
        CREATE, READ, UPDATE, DELETE, LOGIN, LOGOUT, EXPORT, IMPORT, STATUS_CHANGE
    }
}
