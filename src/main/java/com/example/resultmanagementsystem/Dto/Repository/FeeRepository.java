package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Fee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRepository extends MongoRepository<Fee, String> {
    List<Fee> findByStudentId(String studentId);
    List<Fee> findByStudentIdAndAcademicYear(String studentId, String academicYear);
    Optional<Fee> findByStudentIdAndAcademicYearAndSemester(String studentId, String academicYear, String semester);
}