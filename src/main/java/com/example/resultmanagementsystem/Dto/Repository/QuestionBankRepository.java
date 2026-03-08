package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.QuestionBank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {
    List<QuestionBank> findByCourseId(String courseId);
    List<QuestionBank> findByCourseIdAndTopic(String courseId, String topic);
    List<QuestionBank> findByTopic(String topic);
    List<QuestionBank> findByCourseIdAndDifficultyAndIsActiveTrue(String courseId, QuestionBank.Difficulty difficulty);
    List<QuestionBank> findByCourseIdAndIsActiveTrue(String courseId);
    List<QuestionBank> findByTeacherId(String teacherId);
}
