package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student,String> {
    Student findByStudentId(String studentId);

    Student findByEmail(String email);

    Student findByName(String name);
}
