package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findByReceiverIdAndDeletedByReceiverFalseOrderByCreatedAtDesc(String receiverId, Pageable pageable);
    Page<Message> findBySenderIdAndDeletedBySenderFalseOrderByCreatedAtDesc(String senderId, Pageable pageable);
    Page<Message> findByConversationIdOrderByCreatedAtAsc(String conversationId, Pageable pageable);
    List<Message> findByReceiverIdAndIsRead(String receiverId, boolean isRead);
    long countByReceiverIdAndIsRead(String receiverId, boolean isRead);
    List<Message> findByConversationIdOrderByCreatedAtDesc(String conversationId);
    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(String senderId, String receiverId);
}
