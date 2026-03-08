package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.HostelRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelRoomDTO {
    private String id;
    private String hostelId;
    private String roomNumber;
    private int floor;
    private int capacity;
    private int occupiedBeds;
    private HostelRoom.RoomType roomType;
    private HostelRoom.RoomStatus status;
    private List<String> amenities;
    private double monthlyFee;
}
