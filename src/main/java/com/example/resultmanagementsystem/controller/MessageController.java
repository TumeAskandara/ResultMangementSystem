package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.ConversationDTO;
import com.example.resultmanagementsystem.Dto.MessageDTO;
import com.example.resultmanagementsystem.model.Message;
import com.example.resultmanagementsystem.services.CommunicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Messaging", description = "APIs for sending and managing messages between users including inbox, sent messages, conversations, read status, and unread counts.")
public class MessageController {

    private final CommunicationService communicationService;

    @PostMapping
    @Operation(
            summary = "Send a message",
            description = "Sends a new message from one user to another. A conversation ID is automatically generated if not provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message sent successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Invalid message data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Message> sendMessage(
            @Parameter(description = "Message details including sender, receiver, subject, and content", required = true)
            @RequestBody MessageDTO dto) {
        Message message = communicationService.sendMessage(dto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/inbox/{userId}")
    @Operation(
            summary = "Get user inbox",
            description = "Retrieves paginated inbox messages for a specific user, sorted by creation date in descending order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inbox messages retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Message>> getInbox(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable String userId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = communicationService.getInbox(userId, pageable);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/sent/{userId}")
    @Operation(
            summary = "Get sent messages",
            description = "Retrieves paginated sent messages for a specific user, sorted by creation date in descending order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sent messages retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Message>> getSentMessages(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable String userId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = communicationService.getSentMessages(userId, pageable);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/conversation/{conversationId}")
    @Operation(
            summary = "Get conversation messages",
            description = "Retrieves paginated messages in a specific conversation, sorted by creation date in ascending order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation messages retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Message>> getConversation(
            @Parameter(description = "Unique identifier of the conversation", required = true)
            @PathVariable String conversationId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = communicationService.getConversation(conversationId, pageable);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PatchMapping("/{messageId}/read")
    @Operation(
            summary = "Mark message as read",
            description = "Marks a specific message as read and records the read timestamp."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message marked as read successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Message> markAsRead(
            @Parameter(description = "Unique identifier of the message", required = true)
            @PathVariable String messageId) {
        Message message = communicationService.markAsRead(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/read-all/{userId}")
    @Operation(
            summary = "Mark all messages as read",
            description = "Marks all unread messages for a specific user as read."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All messages marked as read successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable String userId) {
        communicationService.markAllAsRead(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "All messages marked as read");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/unread-count/{userId}")
    @Operation(
            summary = "Get unread message count",
            description = "Returns the count of unread messages for a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread count retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable String userId) {
        long count = communicationService.getUnreadCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}/{userId}")
    @Operation(
            summary = "Delete a message",
            description = "Soft-deletes a message for a specific user. The message is only permanently deleted when both sender and receiver have deleted it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, String>> deleteMessage(
            @Parameter(description = "Unique identifier of the message", required = true)
            @PathVariable String messageId,
            @Parameter(description = "Unique identifier of the user performing the delete", required = true)
            @PathVariable String userId) {
        communicationService.deleteMessage(messageId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Message deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/conversations/{userId}")
    @Operation(
            summary = "Get user conversations",
            description = "Retrieves a list of all conversations for a specific user including the other party's name, last message, and unread count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversations retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConversationDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ConversationDTO>> getConversations(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable String userId) {
        List<ConversationDTO> conversations = communicationService.getConversations(userId);
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
