package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends MongoRepository<Staff, String> {

    Optional<Staff> findByEmail(String email);

    List<Staff> findByDepartmentId(String departmentId);

    List<Staff> findByStaffType(Staff.StaffType staffType);

    List<Staff> findByEmploymentStatus(Staff.EmploymentStatus employmentStatus);
}
