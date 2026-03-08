package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscussionPostDTO {
    private String forumId;
    private String authorId;
    private String authorName;
    private String authorRole;
    private String content;
    private String parentPostId;
    private List<String> attachments;
}
