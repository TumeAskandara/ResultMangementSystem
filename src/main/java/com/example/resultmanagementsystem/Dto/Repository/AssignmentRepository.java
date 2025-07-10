package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Assignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    List<Assignment> findByDepartmentIdContainingAndIsActiveTrue(String departmentId);
    List<Assignment> findByDepartmentIdInAndIsActiveTrue(Set<String> departmentIds);
    List<Assignment> findByDepartmentIdAndIsActiveTrue(Set<String> departmentId);
    List<Assignment> findByTeacherIdAndIsActiveTrue(String teacherId);
    List<Assignment> findByDepartmentIdAndDueDateAfterAndIsActiveTrue(String departmentId, LocalDateTime currentDate);
    List<Assignment> findByDepartmentIdAndDueDateBeforeAndIsActiveTrue(String departmentId, LocalDateTime currentDate);
}