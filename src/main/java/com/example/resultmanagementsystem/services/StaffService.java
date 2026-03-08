package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.StaffRepository;
import com.example.resultmanagementsystem.Dto.StaffDTO;
import com.example.resultmanagementsystem.model.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public Staff createStaff(StaffDTO staffDTO) {
        Optional<Staff> existingStaff = staffRepository.findByEmail(staffDTO.getEmail());
        if (existingStaff.isPresent()) {
            throw new RuntimeException("Staff with email already exists: " + staffDTO.getEmail());
        }

        Staff staff = Staff.builder()
                .firstName(staffDTO.getFirstName())
                .lastName(staffDTO.getLastName())
                .email(staffDTO.getEmail())
                .phone(staffDTO.getPhone())
                .address(staffDTO.getAddress())
                .dateOfBirth(staffDTO.getDateOfBirth())
                .gender(staffDTO.getGender())
                .nationality(staffDTO.getNationality())
                .staffType(staffDTO.getStaffType())
                .designation(staffDTO.getDesignation())
                .departmentId(staffDTO.getDepartmentId())
                .qualifications(staffDTO.getQualifications())
                .dateOfJoining(staffDTO.getDateOfJoining())
                .employmentStatus(staffDTO.getEmploymentStatus() != null ? staffDTO.getEmploymentStatus() : Staff.EmploymentStatus.ACTIVE)
                .salary(staffDTO.getSalary())
                .bankAccountNumber(staffDTO.getBankAccountNumber())
                .bankName(staffDTO.getBankName())
                .emergencyContactName(staffDTO.getEmergencyContactName())
                .emergencyContactPhone(staffDTO.getEmergencyContactPhone())
                .profileImageUrl(staffDTO.getProfileImageUrl())
                .userId(staffDTO.getUserId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return staffRepository.save(staff);
    }

    public Staff getStaffById(String id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + id));
    }

    public Staff getStaffByEmail(String email) {
        return staffRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found with email: " + email));
    }

    public Page<Staff> getAllStaff(Pageable pageable) {
        return staffRepository.findAll(pageable);
    }

    public List<Staff> getStaffByDepartment(String departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }

    public List<Staff> getStaffByType(Staff.StaffType staffType) {
        return staffRepository.findByStaffType(staffType);
    }

    public Staff updateStaff(String id, StaffDTO staffDTO) {
        Staff existingStaff = getStaffById(id);

        if (staffDTO.getFirstName() != null) existingStaff.setFirstName(staffDTO.getFirstName());
        if (staffDTO.getLastName() != null) existingStaff.setLastName(staffDTO.getLastName());
        if (staffDTO.getEmail() != null) existingStaff.setEmail(staffDTO.getEmail());
        if (staffDTO.getPhone() != null) existingStaff.setPhone(staffDTO.getPhone());
        if (staffDTO.getAddress() != null) existingStaff.setAddress(staffDTO.getAddress());
        if (staffDTO.getDateOfBirth() != null) existingStaff.setDateOfBirth(staffDTO.getDateOfBirth());
        if (staffDTO.getGender() != null) existingStaff.setGender(staffDTO.getGender());
        if (staffDTO.getNationality() != null) existingStaff.setNationality(staffDTO.getNationality());
        if (staffDTO.getStaffType() != null) existingStaff.setStaffType(staffDTO.getStaffType());
        if (staffDTO.getDesignation() != null) existingStaff.setDesignation(staffDTO.getDesignation());
        if (staffDTO.getDepartmentId() != null) existingStaff.setDepartmentId(staffDTO.getDepartmentId());
        if (staffDTO.getQualifications() != null) existingStaff.setQualifications(staffDTO.getQualifications());
        if (staffDTO.getDateOfJoining() != null) existingStaff.setDateOfJoining(staffDTO.getDateOfJoining());
        if (staffDTO.getEmploymentStatus() != null) existingStaff.setEmploymentStatus(staffDTO.getEmploymentStatus());
        if (staffDTO.getSalary() > 0) existingStaff.setSalary(staffDTO.getSalary());
        if (staffDTO.getBankAccountNumber() != null) existingStaff.setBankAccountNumber(staffDTO.getBankAccountNumber());
        if (staffDTO.getBankName() != null) existingStaff.setBankName(staffDTO.getBankName());
        if (staffDTO.getEmergencyContactName() != null) existingStaff.setEmergencyContactName(staffDTO.getEmergencyContactName());
        if (staffDTO.getEmergencyContactPhone() != null) existingStaff.setEmergencyContactPhone(staffDTO.getEmergencyContactPhone());
        if (staffDTO.getProfileImageUrl() != null) existingStaff.setProfileImageUrl(staffDTO.getProfileImageUrl());
        if (staffDTO.getUserId() != null) existingStaff.setUserId(staffDTO.getUserId());

        existingStaff.setUpdatedAt(LocalDateTime.now());
        return staffRepository.save(existingStaff);
    }

    public Staff updateEmploymentStatus(String id, Staff.EmploymentStatus status) {
        Staff staff = getStaffById(id);
        staff.setEmploymentStatus(status);
        staff.setUpdatedAt(LocalDateTime.now());
        return staffRepository.save(staff);
    }

    public void deleteStaff(String id) {
        Staff staff = getStaffById(id);
        staffRepository.delete(staff);
    }
}
