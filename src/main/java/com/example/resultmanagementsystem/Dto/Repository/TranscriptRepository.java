package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Transcript;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranscriptRepository extends MongoRepository<Transcript, String> {
    List<Transcript> findByStudentId(String studentId);
    Optional<Transcript> findByVerificationCode(String verificationCode);
}
