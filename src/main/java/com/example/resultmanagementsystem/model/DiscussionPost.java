package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "discussion_posts")
public class DiscussionPost {
    @Id
    private String id = UUID.randomUUID().toString();
    private String forumId;
    private String authorId;
    private String authorName;
    private String authorRole;
    private String content;
    private String parentPostId;
    private List<String> attachments;
    private Set<String> likes = new HashSet<>();
    private boolean isEdited;
    private boolean isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
