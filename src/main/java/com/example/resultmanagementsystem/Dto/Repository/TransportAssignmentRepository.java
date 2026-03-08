package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.TransportAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportAssignmentRepository extends MongoRepository<TransportAssignment, String> {
    List<TransportAssignment> findByStudentId(String studentId);
    List<TransportAssignment> findByRouteId(String routeId);
    List<TransportAssignment> findByAcademicYear(String academicYear);
    List<TransportAssignment> findByRouteIdAndStatus(String routeId, TransportAssignment.TransportAssignmentStatus status);
}
