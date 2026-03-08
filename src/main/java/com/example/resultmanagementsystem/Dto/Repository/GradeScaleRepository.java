package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.GradeScale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeScaleRepository extends MongoRepository<GradeScale, String> {
    List<GradeScale> findByDepartmentId(String departmentId);
    Optional<GradeScale> findByIsDefaultTrue();
}
