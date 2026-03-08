package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends MongoRepository<InventoryItem, String> {
    List<InventoryItem> findByDepartmentId(String departmentId);
    List<InventoryItem> findByCategory(String category);

    @Query("{ 'quantity' : { $lte: ?0 } }")
    List<InventoryItem> findByQuantityLessThanEqual(int quantity);

    @Query("{ $expr: { $lte: ['$quantity', '$reorderLevel'] } }")
    List<InventoryItem> findLowStockItems();
}
