package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.DepartmentRepository;
import com.example.resultmanagementsystem.model.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        String departmentName = department.getDepartmentName();
        Optional<Department> existingDepartment = departmentRepository.findByDepartmentName(departmentName);
        {
            if (existingDepartment.isPresent()) {
                throw new RuntimeException("Department Already exist");
            }

            return departmentRepository.save(department);
        }
    }


    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void delete(String departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    public Department updateDepartment(String departmentId, Department updatedDepartment) {
        Department existingDepartment = departmentRepository.findByDepartmentId(departmentId);
        if (existingDepartment != null) {
            existingDepartment.setDepartmentName(updatedDepartment.getDepartmentName());
            return departmentRepository.save(existingDepartment);
        } else {
            throw new RuntimeException("depart not found with id "+ departmentId);
        }
    }
}
