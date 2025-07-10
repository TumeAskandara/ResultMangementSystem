package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student,String> {
    Student findByStudentId(String studentId);

    Optional<Student> findByEmail(String email);

    Student findByName(String name);
    List<Student> findByDepartmentId(String departmentId);

    @Query(value = "{ 'studentId' : ?0 }", fields = "{ 'name' : 1, '_id' : 0 }")
    String findStudentNameByStudentId(String studentId);

    List<Student> findByDepartmentIdIn(List<String> targetDepartments);
}
