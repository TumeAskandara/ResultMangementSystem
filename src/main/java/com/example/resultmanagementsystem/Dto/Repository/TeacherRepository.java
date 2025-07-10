package com.example.resultmanagementsystem.Dto.Repository;


import com.example.resultmanagementsystem.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findByEmail(String email);

    List<Teacher> findByDepartmentId(String departmentId);
}