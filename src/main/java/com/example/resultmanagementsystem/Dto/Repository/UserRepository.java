package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
   Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);
}
