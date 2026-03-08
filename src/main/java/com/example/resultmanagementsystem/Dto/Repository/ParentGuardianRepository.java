package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.ParentGuardian;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentGuardianRepository extends MongoRepository<ParentGuardian, String> {

    Optional<ParentGuardian> findByEmail(String email);

    Optional<ParentGuardian> findByUserId(String userId);

    List<ParentGuardian> findByStudentIdsContaining(String studentId);

    Optional<ParentGuardian> findByPhone(String phone);
}
