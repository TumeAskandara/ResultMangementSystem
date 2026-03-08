package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.TransportRoute;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRouteRepository extends MongoRepository<TransportRoute, String> {
    List<TransportRoute> findByIsActive(boolean isActive);
    List<TransportRoute> findByVehicleId(String vehicleId);
    List<TransportRoute> findByRouteNameContainingIgnoreCase(String routeName);
}
