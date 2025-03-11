package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findByCode(String code);

    List<Course> findByCourseMaster(String courseMaster);

    List<Course> findByCreditsGreaterThanEqual(double credits);

    Course findByCourseTitle(String courseTitle);

    boolean existsByCode(String code);
}