package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.HostelRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelRoomRepository extends MongoRepository<HostelRoom, String> {
    List<HostelRoom> findByHostelId(String hostelId);
    List<HostelRoom> findByHostelIdAndStatus(String hostelId, HostelRoom.RoomStatus status);
    List<HostelRoom> findByStatus(HostelRoom.RoomStatus status);
}
