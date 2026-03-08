package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.InventoryTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransactionDTO {
    private String id;
    private String itemId;
    private String itemName;
    private InventoryTransaction.TransactionType transactionType;
    private int quantity;
    private String reason;
    private String performedBy;
    private String departmentId;
    private String referenceNumber;
}
