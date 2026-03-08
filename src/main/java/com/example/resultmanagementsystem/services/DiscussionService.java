package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.DiscussionForumDTO;
import com.example.resultmanagementsystem.Dto.DiscussionPostDTO;
import com.example.resultmanagementsystem.Dto.Repository.DiscussionForumRepository;
import com.example.resultmanagementsystem.Dto.Repository.DiscussionPostRepository;
import com.example.resultmanagementsystem.model.DiscussionForum;
import com.example.resultmanagementsystem.model.DiscussionPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DiscussionService {

    private final DiscussionForumRepository discussionForumRepository;
    private final DiscussionPostRepository discussionPostRepository;

    // ========== Forum Operations ==========

    public DiscussionForum createForum(DiscussionForumDTO dto) {
        DiscussionForum forum = new DiscussionForum();
        forum.setId(UUID.randomUUID().toString());
        forum.setCourseId(dto.getCourseId());
        forum.setTitle(dto.getTitle());
        forum.setDescription(dto.getDescription());
        forum.setCreatedBy(dto.getCreatedBy());
        forum.setActive(true);
        forum.setPinned(dto.isPinned());
        forum.setCreatedAt(LocalDateTime.now());
        forum.setUpdatedAt(LocalDateTime.now());
        return discussionForumRepository.save(forum);
    }

    public List<DiscussionForum> getForumsByCourse(String courseId) {
        return discussionForumRepository.findByCourseId(courseId);
    }

    // ========== Post Operations ==========

    public DiscussionPost createPost(DiscussionPostDTO dto) {
        DiscussionPost post = new DiscussionPost();
        post.setId(UUID.randomUUID().toString());
        post.setForumId(dto.getForumId());
        post.setAuthorId(dto.getAuthorId());
        post.setAuthorName(dto.getAuthorName());
        post.setAuthorRole(dto.getAuthorRole());
        post.setContent(dto.getContent());
        post.setParentPostId(dto.getParentPostId());
        post.setAttachments(dto.getAttachments());
        post.setLikes(new HashSet<>());
        post.setEdited(false);
        post.setPinned(false);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return discussionPostRepository.save(post);
    }

    public Page<DiscussionPost> getPostsByForum(String forumId, Pageable pageable) {
        return discussionPostRepository.findByForumIdAndParentPostIdIsNull(forumId, pageable);
    }

    public List<DiscussionPost> getReplies(String postId) {
        return discussionPostRepository.findByParentPostIdOrderByCreatedAtAsc(postId);
    }

    public DiscussionPost likePost(String postId, String userId) {
        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        post.getLikes().add(userId);
        return discussionPostRepository.save(post);
    }

    public DiscussionPost unlikePost(String postId, String userId) {
        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        post.getLikes().remove(userId);
        return discussionPostRepository.save(post);
    }

    public DiscussionPost pinPost(String postId) {
        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        post.setPinned(!post.isPinned());
        return discussionPostRepository.save(post);
    }

    public DiscussionPost editPost(String postId, String content, String userId) {
        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        if (!post.getAuthorId().equals(userId)) {
            throw new RuntimeException("You can only edit your own posts");
        }
        post.setContent(content);
        post.setEdited(true);
        post.setUpdatedAt(LocalDateTime.now());
        return discussionPostRepository.save(post);
    }

    public void deletePost(String postId) {
        if (!discussionPostRepository.existsById(postId)) {
            throw new RuntimeException("Post not found with id: " + postId);
        }
        discussionPostRepository.deleteById(postId);
    }
}
