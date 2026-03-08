package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.HostelAllocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostelAllocationRepository extends MongoRepository<HostelAllocation, String> {
    List<HostelAllocation> findByStudentId(String studentId);
    Optional<HostelAllocation> findByStudentIdAndStatus(String studentId, HostelAllocation.AllocationStatus status);
    List<HostelAllocation> findByHostelId(String hostelId);
    List<HostelAllocation> findByHostelIdAndStatus(String hostelId, HostelAllocation.AllocationStatus status);
    List<HostelAllocation> findByRoomId(String roomId);
    List<HostelAllocation> findByRoomIdAndStatus(String roomId, HostelAllocation.AllocationStatus status);
}
