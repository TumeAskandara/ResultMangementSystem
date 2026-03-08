package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStatsDTO {
    private long totalAssets;
    private long activeAssets;
    private long disposedAssets;
    private long assetsUnderRepair;
    private double totalAssetValue;
    private long totalInventoryItems;
    private long lowStockItems;
    private long totalTransactions;
    private double totalInventoryValue;
}
