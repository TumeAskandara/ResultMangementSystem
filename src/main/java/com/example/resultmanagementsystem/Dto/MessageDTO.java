package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDTO {
    private String id;
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
    private String parentMessageId;
    private String conversationId;
    private LocalDateTime createdAt;
}
