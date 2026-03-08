package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemDTO {
    private String id;
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
}
