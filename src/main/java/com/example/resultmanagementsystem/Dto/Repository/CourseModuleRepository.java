package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.CourseModule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseModuleRepository extends MongoRepository<CourseModule, String> {
    List<CourseModule> findByCourseIdOrderByOrderIndexAsc(String courseId);
}
