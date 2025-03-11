package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    Department findByDepartmentId(String departmentId);

//    List<Department> getAll();

    Optional<Department> findByDepartmentName(String departmentName);
}
