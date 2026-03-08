package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.LearningProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningProgressRepository extends MongoRepository<LearningProgress, String> {
    List<LearningProgress> findByStudentIdAndCourseId(String studentId, String courseId);
    Optional<LearningProgress> findByStudentIdAndLessonId(String studentId, String lessonId);
}
