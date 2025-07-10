package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByAssignmentId(String assignmentId);
    List<Submission> findByStudentId(String studentId);
    Optional<Submission> findByAssignmentIdAndStudentId(String assignmentId, String studentId);
    List<Submission> findByAssignmentIdIn(List<String> assignmentIds);

    List<Submission> findByAssignmentIdInAndGradeIsNull(List<String> assignmentIds);

    List<Submission> findByAssignmentIdInAndGradeIsNotNull(List<String> assignmentIds);
}