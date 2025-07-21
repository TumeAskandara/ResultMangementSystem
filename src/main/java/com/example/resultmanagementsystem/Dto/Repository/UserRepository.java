package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query("{'emailVerificationToken': ?0}")
    Optional<User> findByEmailVerificationToken(String token);

    @Query("{'verificationToken': ?0}")
    Optional<User> findByVerificationToken(String token);

    boolean existsByEmail(String email);

    @Query("{'studentId': ?0}")
    Optional<User> findByStudentId(String studentId);

    @Query("{'departmentId': ?0}")
    Optional<User> findByDepartmentId(String departmentId);
}
