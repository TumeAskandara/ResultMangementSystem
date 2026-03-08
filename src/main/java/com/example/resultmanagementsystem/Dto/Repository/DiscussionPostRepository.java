package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.DiscussionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionPostRepository extends MongoRepository<DiscussionPost, String> {
    Page<DiscussionPost> findByForumIdAndParentPostIdIsNull(String forumId, Pageable pageable);
    List<DiscussionPost> findByParentPostIdOrderByCreatedAtAsc(String parentPostId);
}
