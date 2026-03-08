package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    Page<AuditLog> findByPerformedBy(String performedBy, Pageable pageable);
    Page<AuditLog> findByAction(AuditLog.AuditAction action, Pageable pageable);
    Page<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<AuditLog> findByPerformedByAndTimestampBetween(String performedBy, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);
    List<AuditLog> findTopByOrderByTimestampDesc();
}
