package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.HostelMaintenanceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelMaintenanceRequestRepository extends MongoRepository<HostelMaintenanceRequest, String> {
    List<HostelMaintenanceRequest> findByHostelId(String hostelId);
    List<HostelMaintenanceRequest> findByRoomId(String roomId);
    List<HostelMaintenanceRequest> findByStatus(HostelMaintenanceRequest.MaintenanceStatus status);
    List<HostelMaintenanceRequest> findByHostelIdAndStatus(String hostelId, HostelMaintenanceRequest.MaintenanceStatus status);
}
