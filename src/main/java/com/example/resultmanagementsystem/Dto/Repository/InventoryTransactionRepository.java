package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.InventoryTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryTransactionRepository extends MongoRepository<InventoryTransaction, String> {
    List<InventoryTransaction> findByItemId(String itemId);
    List<InventoryTransaction> findByDepartmentId(String departmentId);
    List<InventoryTransaction> findByTransactionType(InventoryTransaction.TransactionType transactionType);
    List<InventoryTransaction> findByPerformedBy(String performedBy);
}
