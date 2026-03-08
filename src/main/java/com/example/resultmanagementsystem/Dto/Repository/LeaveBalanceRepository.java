package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.LeaveBalance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends MongoRepository<LeaveBalance, String> {

    Optional<LeaveBalance> findByStaffIdAndAcademicYear(String staffId, String academicYear);
}
