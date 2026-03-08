package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "messages")
public class Message {
    @Id
    private String id = UUID.randomUUID().toString();

    private String senderId;
    private String senderName;
    private String senderRole;
    private String receiverId;
    private String receiverName;
    private String subject;
    private String content;
    private List<String> attachments;
    private boolean isRead;
    private LocalDateTime readAt;
    private boolean isDeleted;
    private boolean deletedBySender;
    private boolean deletedByReceiver;
    private String parentMessageId;
    private String conversationId;
    private LocalDateTime createdAt;
}
