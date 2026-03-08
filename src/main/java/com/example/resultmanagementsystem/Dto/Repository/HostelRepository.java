package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Hostel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelRepository extends MongoRepository<Hostel, String> {
    List<Hostel> findByIsActive(boolean isActive);
    List<Hostel> findByType(Hostel.HostelType type);
}
