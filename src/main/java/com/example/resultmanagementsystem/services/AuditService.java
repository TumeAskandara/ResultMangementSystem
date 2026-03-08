package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.AuditLogDTO;
import com.example.resultmanagementsystem.Dto.AuditSearchDTO;
import com.example.resultmanagementsystem.Dto.Repository.AuditLogRepository;
import com.example.resultmanagementsystem.model.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final MongoTemplate mongoTemplate;

    public AuditLog log(String action, String entityType, String entityId, String performedBy,
                        String performedByRole, String performedByEmail, String details,
                        String previousValue, String newValue) {
        AuditLog auditLog = AuditLog.builder()
                .id(UUID.randomUUID().toString())
                .action(AuditLog.AuditAction.valueOf(action.toUpperCase()))
                .entityType(entityType)
                .entityId(entityId)
                .performedBy(performedBy)
                .performedByRole(performedByRole)
                .performedByEmail(performedByEmail)
                .details(details)
                .previousValue(previousValue)
                .newValue(newValue)
                .timestamp(LocalDateTime.now())
                .build();

        return auditLogRepository.save(auditLog);
    }

    public List<AuditLogDTO> getAuditLogsByEntity(String entityType, String entityId) {
        List<AuditLog> logs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return logs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Page<AuditLogDTO> getAuditLogsByUser(String performedBy, Pageable pageable) {
        return auditLogRepository.findByPerformedBy(performedBy, pageable).map(this::toDTO);
    }

    public Page<AuditLogDTO> getAuditLogsByAction(String action, Pageable pageable) {
        AuditLog.AuditAction auditAction = AuditLog.AuditAction.valueOf(action.toUpperCase());
        return auditLogRepository.findByAction(auditAction, pageable).map(this::toDTO);
    }

    public Page<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return auditLogRepository.findByTimestampBetween(start, end, pageable).map(this::toDTO);
    }

    public Page<AuditLogDTO> searchAuditLogs(AuditSearchDTO searchDTO, Pageable pageable) {
        Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "timestamp"));
        List<Criteria> criteriaList = new ArrayList<>();

        if (searchDTO.getEntityType() != null && !searchDTO.getEntityType().isEmpty()) {
            criteriaList.add(Criteria.where("entityType").is(searchDTO.getEntityType()));
        }
        if (searchDTO.getEntityId() != null && !searchDTO.getEntityId().isEmpty()) {
            criteriaList.add(Criteria.where("entityId").is(searchDTO.getEntityId()));
        }
        if (searchDTO.getPerformedBy() != null && !searchDTO.getPerformedBy().isEmpty()) {
            criteriaList.add(Criteria.where("performedBy").is(searchDTO.getPerformedBy()));
        }
        if (searchDTO.getAction() != null) {
            criteriaList.add(Criteria.where("action").is(searchDTO.getAction()));
        }
        if (searchDTO.getStartDate() != null) {
            criteriaList.add(Criteria.where("timestamp").gte(searchDTO.getStartDate()));
        }
        if (searchDTO.getEndDate() != null) {
            criteriaList.add(Criteria.where("timestamp").lte(searchDTO.getEndDate()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        List<AuditLog> logs = mongoTemplate.find(query, AuditLog.class);
        List<AuditLogDTO> dtos = logs.stream().map(this::toDTO).collect(Collectors.toList());

        Query countQuery = new Query();
        if (!criteriaList.isEmpty()) {
            countQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return PageableExecutionUtils.getPage(dtos, pageable,
                () -> mongoTemplate.count(countQuery, AuditLog.class));
    }

    public List<AuditLogDTO> getRecentActivity(int limit) {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(limit);
        List<AuditLog> logs = mongoTemplate.find(query, AuditLog.class);
        return logs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AuditLogDTO toDTO(AuditLog log) {
        return AuditLogDTO.builder()
                .id(log.getId())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .performedBy(log.getPerformedBy())
                .performedByRole(log.getPerformedByRole())
                .performedByEmail(log.getPerformedByEmail())
                .details(log.getDetails())
                .previousValue(log.getPreviousValue())
                .newValue(log.getNewValue())
                .ipAddress(log.getIpAddress())
                .userAgent(log.getUserAgent())
                .timestamp(log.getTimestamp())
                .build();
    }
}
