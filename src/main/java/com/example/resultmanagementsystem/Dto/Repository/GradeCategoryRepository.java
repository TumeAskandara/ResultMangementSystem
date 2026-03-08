package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.GradeCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeCategoryRepository extends MongoRepository<GradeCategory, String> {
    List<GradeCategory> findByCourseId(String courseId);
    List<GradeCategory> findByDepartmentId(String departmentId);
}
