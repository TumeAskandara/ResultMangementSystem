package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Asset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDTO {
    private String id;
    private String assetName;
    private String assetCode;
    private Asset.AssetCategory category;
    private String description;
    private LocalDate purchaseDate;
    private double purchasePrice;
    private double currentValue;
    private String location;
    private String departmentId;
    private String assignedTo;
    private Asset.AssetCondition condition;
    private String warranty;
    private LocalDate warrantyExpiry;
    private String serialNumber;
    private String vendor;
    private Asset.AssetStatus status;
}
