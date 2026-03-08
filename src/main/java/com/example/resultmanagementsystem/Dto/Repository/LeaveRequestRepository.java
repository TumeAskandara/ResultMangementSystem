package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {

    List<LeaveRequest> findByStaffId(String staffId);

    Page<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status, Pageable pageable);

    List<LeaveRequest> findByStaffIdAndStatus(String staffId, LeaveRequest.LeaveStatus status);

    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
