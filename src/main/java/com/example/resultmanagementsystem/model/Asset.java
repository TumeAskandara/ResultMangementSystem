package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "assets")
public class Asset {
    @Id
    private String id = UUID.randomUUID().toString();
    private String assetName;
    private String assetCode;
    private AssetCategory category;
    private String description;
    private LocalDate purchaseDate;
    private double purchasePrice;
    private double currentValue;
    private String location;
    private String departmentId;
    private String assignedTo;
    private AssetCondition condition;
    private String warranty;
    private LocalDate warrantyExpiry;
    private String serialNumber;
    private String vendor;
    private AssetStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum AssetCategory {
        FURNITURE, ELECTRONICS, LAB_EQUIPMENT, SPORTS, VEHICLE, BOOKS, OTHER
    }

    public enum AssetCondition {
        NEW, GOOD, FAIR, POOR, DAMAGED, DISPOSED
    }

    public enum AssetStatus {
        ACTIVE, UNDER_REPAIR, DISPOSED, LOST
    }
}
