package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.NotificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends MongoRepository<NotificationLog, String> {
    List<NotificationLog> findByEventId(String eventId);
    List<NotificationLog> findByRecipientStudentIdsContaining(String studentId);
}