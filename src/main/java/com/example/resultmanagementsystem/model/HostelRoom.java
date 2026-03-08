package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "hostel_rooms")
public class HostelRoom {
    @Id
    private String id = UUID.randomUUID().toString();
    private String hostelId;
    private String roomNumber;
    private int floor;
    private int capacity;
    private int occupiedBeds;
    private RoomType roomType;
    private RoomStatus status;
    @Builder.Default
    private List<String> amenities = new ArrayList<>();
    private double monthlyFee;

    public enum RoomType {
        SINGLE, DOUBLE, TRIPLE, DORMITORY
    }

    public enum RoomStatus {
        AVAILABLE, FULL, MAINTENANCE, CLOSED
    }
}
