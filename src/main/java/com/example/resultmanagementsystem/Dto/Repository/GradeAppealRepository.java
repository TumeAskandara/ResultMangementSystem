package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.GradeAppeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeAppealRepository extends MongoRepository<GradeAppeal, String> {
    List<GradeAppeal> findByStudentId(String studentId);
    List<GradeAppeal> findByCourseId(String courseId);
    Page<GradeAppeal> findByStatus(GradeAppeal.AppealStatus status, Pageable pageable);
}
