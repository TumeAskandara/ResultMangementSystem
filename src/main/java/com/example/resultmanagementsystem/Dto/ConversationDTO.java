package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConversationDTO {
    private String conversationId;
    private String otherPartyName;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
    private long unreadCount;
}
