package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.SalaryStructure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryStructureRepository extends MongoRepository<SalaryStructure, String> {

    Optional<SalaryStructure> findByDesignation(String designation);

    List<SalaryStructure> findByIsActive(boolean isActive);
}
