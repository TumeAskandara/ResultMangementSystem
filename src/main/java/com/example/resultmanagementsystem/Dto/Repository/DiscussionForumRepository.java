package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.DiscussionForum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionForumRepository extends MongoRepository<DiscussionForum, String> {
    List<DiscussionForum> findByCourseId(String courseId);
}
