package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "inventory_transactions")
public class InventoryTransaction {
    @Id
    private String id = UUID.randomUUID().toString();
    private String itemId;
    private String itemName;
    private TransactionType transactionType;
    private int quantity;
    private int previousQuantity;
    private int newQuantity;
    private String reason;
    private String performedBy;
    private String departmentId;
    private String referenceNumber;
    private LocalDateTime transactionDate;

    public enum TransactionType {
        STOCK_IN, STOCK_OUT, ADJUSTMENT, TRANSFER
    }
}
