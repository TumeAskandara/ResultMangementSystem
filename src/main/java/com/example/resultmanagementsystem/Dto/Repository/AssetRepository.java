package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {
    List<Asset> findByDepartmentId(String departmentId);
    List<Asset> findByCategory(Asset.AssetCategory category);
    List<Asset> findByStatus(Asset.AssetStatus status);
    List<Asset> findByCondition(Asset.AssetCondition condition);
}
