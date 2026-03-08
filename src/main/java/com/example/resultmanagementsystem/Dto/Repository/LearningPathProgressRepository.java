package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.LearningPathProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningPathProgressRepository extends MongoRepository<LearningPathProgress, String> {
    List<LearningPathProgress> findByStudentId(String studentId);
    List<LearningPathProgress> findByLearningPathId(String learningPathId);
    Optional<LearningPathProgress> findByStudentIdAndLearningPathId(String studentId, String learningPathId);
}
