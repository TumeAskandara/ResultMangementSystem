package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    List<Vehicle> findByRouteId(String routeId);
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
}
