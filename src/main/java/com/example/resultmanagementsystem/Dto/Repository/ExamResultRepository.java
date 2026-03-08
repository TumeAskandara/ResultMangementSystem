package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.ExamResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends MongoRepository<ExamResult, String> {
    List<ExamResult> findByExamId(String examId);
    List<ExamResult> findByStudentId(String studentId);
    List<ExamResult> findByExamIdAndStudentId(String examId, String studentId);
    List<ExamResult> findByStatus(ExamResult.ExamResultStatus status);
}
