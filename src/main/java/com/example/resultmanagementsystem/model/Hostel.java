package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "hostels")
public class Hostel {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private HostelType type;
    private String address;
    private String wardenId;
    private String wardenName;
    private int totalRooms;
    private int totalCapacity;
    private int occupiedBeds;
    @Builder.Default
    private List<String> amenities = new ArrayList<>();
    private String contactPhone;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum HostelType {
        BOYS, GIRLS, MIXED
    }
}
