package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.Hostel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelDTO {
    private String id;
    private String name;
    private Hostel.HostelType type;
    private String address;
    private String wardenId;
    private String wardenName;
    private int totalRooms;
    private int totalCapacity;
    private int occupiedBeds;
    private List<String> amenities;
    private String contactPhone;
    private boolean isActive;
}
