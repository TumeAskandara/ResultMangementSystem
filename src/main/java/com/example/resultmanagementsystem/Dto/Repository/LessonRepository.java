package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {
    List<Lesson> findByModuleIdOrderByOrderIndexAsc(String moduleId);
    List<Lesson> findByCourseIdOrderByOrderIndexAsc(String courseId);
}
