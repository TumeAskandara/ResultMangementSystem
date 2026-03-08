package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.LearningPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends MongoRepository<LearningPath, String> {
    List<LearningPath> findByDepartmentId(String departmentId);
    Page<LearningPath> findByIsPublished(boolean isPublished, Pageable pageable);
    List<LearningPath> findByEnrolledStudentsContaining(String studentId);
    List<LearningPath> findByTagsContaining(String tag);
}
