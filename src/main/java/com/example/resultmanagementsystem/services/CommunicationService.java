package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.AnnouncementDTO;
import com.example.resultmanagementsystem.Dto.ConversationDTO;
import com.example.resultmanagementsystem.Dto.MessageDTO;
import com.example.resultmanagementsystem.Dto.Repository.AnnouncementRepository;
import com.example.resultmanagementsystem.Dto.Repository.MessageRepository;
import com.example.resultmanagementsystem.model.Announcement;
import com.example.resultmanagementsystem.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommunicationService {

    private final AnnouncementRepository announcementRepository;
    private final MessageRepository messageRepository;

    // ===== Announcement Methods =====

    public Announcement createAnnouncement(AnnouncementDTO dto) {
        Announcement announcement = Announcement.builder()
                .id(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .priority(dto.getPriority())
                .targetAudience(dto.getTargetAudience())
                .departmentId(dto.getDepartmentId())
                .attachments(dto.getAttachments())
                .publishedBy(dto.getPublishedBy())
                .publishedByName(dto.getPublishedByName())
                .publishDate(dto.getPublishDate() != null ? dto.getPublishDate() : LocalDateTime.now())
                .expiryDate(dto.getExpiryDate())
                .isActive(true)
                .isPinned(dto.isPinned())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return announcementRepository.save(announcement);
    }

    public Announcement getAnnouncementById(String id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with ID: " + id));
    }

    public Page<Announcement> getActiveAnnouncements(Pageable pageable) {
        return announcementRepository.findByIsActive(true, pageable);
    }

    public Page<Announcement> getAnnouncementsByType(String type, Pageable pageable) {
        Announcement.AnnouncementType announcementType = Announcement.AnnouncementType.valueOf(type.toUpperCase());
        return announcementRepository.findByType(announcementType, pageable);
    }

    public Page<Announcement> getAnnouncementsByAudience(String audience, Pageable pageable) {
        Announcement.TargetAudience targetAudience = Announcement.TargetAudience.valueOf(audience.toUpperCase());
        return announcementRepository.findByTargetAudience(targetAudience, pageable);
    }

    public List<Announcement> getPinnedAnnouncements() {
        return announcementRepository.findByIsPinnedAndIsActive(true, true);
    }

    public Announcement updateAnnouncement(String id, AnnouncementDTO dto) {
        Announcement existing = getAnnouncementById(id);

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getContent() != null) existing.setContent(dto.getContent());
        if (dto.getType() != null) existing.setType(dto.getType());
        if (dto.getPriority() != null) existing.setPriority(dto.getPriority());
        if (dto.getTargetAudience() != null) existing.setTargetAudience(dto.getTargetAudience());
        if (dto.getDepartmentId() != null) existing.setDepartmentId(dto.getDepartmentId());
        if (dto.getAttachments() != null) existing.setAttachments(dto.getAttachments());
        if (dto.getExpiryDate() != null) existing.setExpiryDate(dto.getExpiryDate());
        existing.setPinned(dto.isPinned());
        existing.setUpdatedAt(LocalDateTime.now());

        return announcementRepository.save(existing);
    }

    public Announcement deactivateAnnouncement(String id) {
        Announcement announcement = getAnnouncementById(id);
        announcement.setActive(false);
        announcement.setUpdatedAt(LocalDateTime.now());
        return announcementRepository.save(announcement);
    }

    // ===== Message Methods =====

    public Message sendMessage(MessageDTO dto) {
        String conversationId = dto.getConversationId();
        if (conversationId == null || conversationId.isEmpty()) {
            // Generate a deterministic conversation ID based on both parties
            String id1 = dto.getSenderId().compareTo(dto.getReceiverId()) < 0 ?
                    dto.getSenderId() : dto.getReceiverId();
            String id2 = dto.getSenderId().compareTo(dto.getReceiverId()) < 0 ?
                    dto.getReceiverId() : dto.getSenderId();
            conversationId = id1 + "_" + id2;
        }

        Message message = Message.builder()
                .id(UUID.randomUUID().toString())
                .senderId(dto.getSenderId())
                .senderName(dto.getSenderName())
                .senderRole(dto.getSenderRole())
                .receiverId(dto.getReceiverId())
                .receiverName(dto.getReceiverName())
                .subject(dto.getSubject())
                .content(dto.getContent())
                .attachments(dto.getAttachments())
                .isRead(false)
                .isDeleted(false)
                .deletedBySender(false)
                .deletedByReceiver(false)
                .parentMessageId(dto.getParentMessageId())
                .conversationId(conversationId)
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    public Page<Message> getInbox(String userId, Pageable pageable) {
        return messageRepository.findByReceiverIdAndDeletedByReceiverFalseOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Message> getSentMessages(String userId, Pageable pageable) {
        return messageRepository.findBySenderIdAndDeletedBySenderFalseOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Message> getConversation(String conversationId, Pageable pageable) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId, pageable);
    }

    public Message markAsRead(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));
        message.setRead(true);
        message.setReadAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public void markAllAsRead(String userId) {
        List<Message> unreadMessages = messageRepository.findByReceiverIdAndIsRead(userId, false);
        for (Message message : unreadMessages) {
            message.setRead(true);
            message.setReadAt(LocalDateTime.now());
        }
        messageRepository.saveAll(unreadMessages);
    }

    public long getUnreadCount(String userId) {
        return messageRepository.countByReceiverIdAndIsRead(userId, false);
    }

    public void deleteMessage(String messageId, String userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        if (userId.equals(message.getSenderId())) {
            message.setDeletedBySender(true);
        }
        if (userId.equals(message.getReceiverId())) {
            message.setDeletedByReceiver(true);
        }

        if (message.isDeletedBySender() && message.isDeletedByReceiver()) {
            message.setDeleted(true);
        }

        messageRepository.save(message);
    }

    public List<ConversationDTO> getConversations(String userId) {
        List<Message> allMessages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);

        Map<String, List<Message>> conversationMap = allMessages.stream()
                .filter(m -> !(userId.equals(m.getSenderId()) && m.isDeletedBySender())
                        && !(userId.equals(m.getReceiverId()) && m.isDeletedByReceiver()))
                .collect(Collectors.groupingBy(Message::getConversationId));

        List<ConversationDTO> conversations = new ArrayList<>();
        for (Map.Entry<String, List<Message>> entry : conversationMap.entrySet()) {
            List<Message> messages = entry.getValue();
            if (messages.isEmpty()) continue;

            Message lastMsg = messages.get(0); // already sorted desc

            String otherPartyName;
            if (userId.equals(lastMsg.getSenderId())) {
                otherPartyName = lastMsg.getReceiverName();
            } else {
                otherPartyName = lastMsg.getSenderName();
            }

            long unreadCount = messages.stream()
                    .filter(m -> userId.equals(m.getReceiverId()) && !m.isRead())
                    .count();

            conversations.add(ConversationDTO.builder()
                    .conversationId(entry.getKey())
                    .otherPartyName(otherPartyName)
                    .lastMessage(lastMsg.getContent())
                    .lastMessageDate(lastMsg.getCreatedAt())
                    .unreadCount(unreadCount)
                    .build());
        }

        conversations.sort((a, b) -> b.getLastMessageDate().compareTo(a.getLastMessageDate()));
        return conversations;
    }
}
