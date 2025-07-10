package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByRecipientIdsContaining(String recipientId);
    List<Notification> findByDepartmentId(String departmentId);
    Set<Notification> findByDepartmentIdAndRecipientType(String departmentId, String recipientType);





    @Query("{'scheduledDate': {$lte: ?0}, 'sent': false, 'status': 'PENDING'}")
    List<Notification> findPendingNotificationsToSend(LocalDateTime currentTime);

    @Query("{'recipientIds': {$in: [?0]}, 'isRead': false}")
    List<Notification> findUnreadNotificationsByRecipient(String recipientId);
}