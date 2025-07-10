package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

    Optional<Course> findByCode(String code);

    List<Course> findByTeacherId(String teacherId);

    List<Course> findByCreditsGreaterThanEqual(double credits);

    List<Course> findCourseByDepartmentId(String departmentId);

    Course findByCourseTitle(String courseTitle);

    boolean existsByCode(String code);

    @Query(value = "{ '_id' : ?0 }", fields = "{ 'courseTitle' : 1, 'courseMaster' : 1, 'credits': 1, 'code': 1}")
    Optional<Course> findCourseTitleAndMasterById(String id);

}
