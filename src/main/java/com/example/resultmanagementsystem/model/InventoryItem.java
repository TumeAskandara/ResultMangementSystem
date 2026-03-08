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
@Document(collection = "inventory_items")
public class InventoryItem {
    @Id
    private String id = UUID.randomUUID().toString();
    private String itemName;
    private String itemCode;
    private String category;
    private String description;
    private int quantity;
    private String unit;
    private int reorderLevel;
    private double unitPrice;
    private String location;
    private String departmentId;
    private String supplier;
    private LocalDateTime lastRestockedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
