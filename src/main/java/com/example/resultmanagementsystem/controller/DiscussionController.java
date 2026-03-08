package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.DiscussionForumDTO;
import com.example.resultmanagementsystem.Dto.DiscussionPostDTO;
import com.example.resultmanagementsystem.model.DiscussionForum;
import com.example.resultmanagementsystem.model.DiscussionPost;
import com.example.resultmanagementsystem.services.DiscussionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/discussions")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Discussion Forum Management", description = "APIs for managing discussion forums, posts, replies, likes, and post moderation")
public class DiscussionController {

    private final DiscussionService discussionService;

    @Autowired
    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    // ========== Forum Endpoints ==========

    @PostMapping("/forums")
    @Operation(summary = "Create a discussion forum", description = "Creates a new discussion forum associated with a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Forum created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionForum.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<DiscussionForum> createForum(
            @Parameter(description = "Forum data", required = true) @RequestBody DiscussionForumDTO dto) {
        DiscussionForum forum = discussionService.createForum(dto);
        return new ResponseEntity<>(forum, HttpStatus.CREATED);
    }

    @GetMapping("/forums/course/{courseId}")
    @Operation(summary = "Get forums by course", description = "Retrieves all discussion forums for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forums retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionForum.class))),
            @ApiResponse(responseCode = "404", description = "No forums found for the course",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<DiscussionForum>> getForumsByCourse(
            @Parameter(description = "Course ID", required = true, example = "CSC101") @PathVariable String courseId) {
        return ResponseEntity.ok(discussionService.getForumsByCourse(courseId));
    }

    // ========== Post Endpoints ==========

    @PostMapping("/posts")
    @Operation(summary = "Create a discussion post", description = "Creates a new post in a forum. Set parentPostId to reply to an existing post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionPost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid post data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<DiscussionPost> createPost(
            @Parameter(description = "Post data", required = true) @RequestBody DiscussionPostDTO dto) {
        DiscussionPost post = discussionService.createPost(dto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/posts/forum/{forumId}")
    @Operation(summary = "Get posts by forum", description = "Retrieves top-level posts in a forum with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "No posts found for the forum",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<DiscussionPost>> getPostsByForum(
            @Parameter(description = "Forum ID", required = true) @PathVariable String forumId,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(discussionService.getPostsByForum(forumId, pageable));
    }

    @GetMapping("/posts/{postId}/replies")
    @Operation(summary = "Get replies to a post", description = "Retrieves all replies to a specific post ordered by creation time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Replies retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionPost.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<DiscussionPost>> getReplies(
            @Parameter(description = "Post ID", required = true) @PathVariable String postId) {
        return ResponseEntity.ok(discussionService.getReplies(postId));
    }

    @PostMapping("/posts/{postId}/like")
    @Operation(summary = "Like a post", description = "Adds a like from a user to a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post liked successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionPost.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<DiscussionPost> likePost(
            @Parameter(description = "Post ID", required = true) @PathVariable String postId,
            @Parameter(description = "User ID", required = true) @RequestParam String userId) {
        return ResponseEntity.ok(discussionService.likePost(postId, userId));
    }

    @DeleteMapping("/posts/{postId}/unlike")
    @Operation(summary = "Unlike a post", description = "Removes a like from a user on a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post unliked successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionPost.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<DiscussionPost> unlikePost(
            @Parameter(description = "Post ID", required = true) @PathVariable String postId,
            @Parameter(description = "User ID", required = true) @RequestParam String userId) {
        return ResponseEntity.ok(discussionService.unlikePost(postId, userId));
    }

    @PatchMapping("/posts/{postId}/pin")
    @Operation(summary = "Toggle pin on a post", description = "Toggles the pinned status of a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post pin status toggled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionPost.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<DiscussionPost> pinPost(
            @Parameter(description = "Post ID", required = true) @PathVariable String postId) {
        return ResponseEntity.ok(discussionService.pinPost(postId));
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "Edit a post", description = "Edits the content of a post. Only the original author can edit their post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post edited successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscussionPost.class))),
            @ApiResponse(responseCode = "403", description = "Not authorized to edit this post",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<DiscussionPost> editPost(
            @Parameter(description = "Post ID", required = true) @PathVariable String postId,
            @Parameter(description = "Updated content and user ID", required = true) @RequestBody Map<String, String> request) {
        String content = request.get("content");
        String userId = request.get("userId");
        return ResponseEntity.ok(discussionService.editPost(postId, content, userId));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Delete a post", description = "Permanently deletes a discussion post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> deletePost(
            @Parameter(description = "Post ID", required = true) @PathVariable String postId) {
        discussionService.deletePost(postId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post with ID " + postId + " successfully deleted");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
