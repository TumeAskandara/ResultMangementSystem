package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.HostelAllocationDTO;
import com.example.resultmanagementsystem.Dto.HostelDTO;
import com.example.resultmanagementsystem.Dto.HostelRoomDTO;
import com.example.resultmanagementsystem.Dto.HostelStatsDTO;
import com.example.resultmanagementsystem.Dto.MaintenanceRequestDTO;
import com.example.resultmanagementsystem.Dto.Repository.HostelAllocationRepository;
import com.example.resultmanagementsystem.Dto.Repository.HostelMaintenanceRequestRepository;
import com.example.resultmanagementsystem.Dto.Repository.HostelRepository;
import com.example.resultmanagementsystem.Dto.Repository.HostelRoomRepository;
import com.example.resultmanagementsystem.model.Hostel;
import com.example.resultmanagementsystem.model.HostelAllocation;
import com.example.resultmanagementsystem.model.HostelMaintenanceRequest;
import com.example.resultmanagementsystem.model.HostelRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class HostelService {

    private final HostelRepository hostelRepository;
    private final HostelRoomRepository hostelRoomRepository;
    private final HostelAllocationRepository hostelAllocationRepository;
    private final HostelMaintenanceRequestRepository hostelMaintenanceRequestRepository;

    // ==================== HOSTEL MANAGEMENT ====================

    public Hostel createHostel(HostelDTO dto) {
        log.info("Creating new hostel: {}", dto.getName());
        Hostel hostel = Hostel.builder()
                .id(UUID.randomUUID().toString())
                .name(dto.getName())
                .type(dto.getType())
                .address(dto.getAddress())
                .wardenId(dto.getWardenId())
                .wardenName(dto.getWardenName())
                .totalRooms(dto.getTotalRooms())
                .totalCapacity(dto.getTotalCapacity())
                .occupiedBeds(0)
                .amenities(dto.getAmenities() != null ? dto.getAmenities() : new ArrayList<>())
                .contactPhone(dto.getContactPhone())
                .isActive(dto.isActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return hostelRepository.save(hostel);
    }

    public Hostel getHostelById(String id) {
        return hostelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hostel not found with id: " + id));
    }

    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    public Hostel updateHostel(String id, HostelDTO dto) {
        Hostel hostel = getHostelById(id);
        if (dto.getName() != null) hostel.setName(dto.getName());
        if (dto.getType() != null) hostel.setType(dto.getType());
        if (dto.getAddress() != null) hostel.setAddress(dto.getAddress());
        if (dto.getWardenId() != null) hostel.setWardenId(dto.getWardenId());
        if (dto.getWardenName() != null) hostel.setWardenName(dto.getWardenName());
        if (dto.getTotalRooms() > 0) hostel.setTotalRooms(dto.getTotalRooms());
        if (dto.getTotalCapacity() > 0) hostel.setTotalCapacity(dto.getTotalCapacity());
        if (dto.getAmenities() != null) hostel.setAmenities(dto.getAmenities());
        if (dto.getContactPhone() != null) hostel.setContactPhone(dto.getContactPhone());
        hostel.setActive(dto.isActive());
        hostel.setUpdatedAt(LocalDateTime.now());
        return hostelRepository.save(hostel);
    }

    // ==================== ROOM MANAGEMENT ====================

    public HostelRoom addRoom(HostelRoomDTO dto) {
        log.info("Adding room {} to hostel {}", dto.getRoomNumber(), dto.getHostelId());
        HostelRoom room = HostelRoom.builder()
                .id(UUID.randomUUID().toString())
                .hostelId(dto.getHostelId())
                .roomNumber(dto.getRoomNumber())
                .floor(dto.getFloor())
                .capacity(dto.getCapacity())
                .occupiedBeds(0)
                .roomType(dto.getRoomType())
                .status(HostelRoom.RoomStatus.AVAILABLE)
                .amenities(dto.getAmenities() != null ? dto.getAmenities() : new ArrayList<>())
                .monthlyFee(dto.getMonthlyFee())
                .build();
        return hostelRoomRepository.save(room);
    }

    public List<HostelRoom> getRoomsByHostel(String hostelId) {
        return hostelRoomRepository.findByHostelId(hostelId);
    }

    public List<HostelRoom> getAvailableRooms() {
        return hostelRoomRepository.findByStatus(HostelRoom.RoomStatus.AVAILABLE);
    }

    public HostelRoom updateRoom(String id, HostelRoomDTO dto) {
        HostelRoom room = hostelRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        if (dto.getRoomNumber() != null) room.setRoomNumber(dto.getRoomNumber());
        if (dto.getFloor() >= 0) room.setFloor(dto.getFloor());
        if (dto.getCapacity() > 0) room.setCapacity(dto.getCapacity());
        if (dto.getRoomType() != null) room.setRoomType(dto.getRoomType());
        if (dto.getStatus() != null) room.setStatus(dto.getStatus());
        if (dto.getAmenities() != null) room.setAmenities(dto.getAmenities());
        if (dto.getMonthlyFee() > 0) room.setMonthlyFee(dto.getMonthlyFee());
        return hostelRoomRepository.save(room);
    }

    // ==================== ALLOCATION MANAGEMENT ====================

    public HostelAllocation allocateStudent(HostelAllocationDTO dto) {
        log.info("Allocating student {} to hostel {}", dto.getStudentId(), dto.getHostelId());

        // Update room occupancy
        HostelRoom room = hostelRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + dto.getRoomId()));
        if (room.getOccupiedBeds() >= room.getCapacity()) {
            throw new RuntimeException("Room is already full");
        }
        room.setOccupiedBeds(room.getOccupiedBeds() + 1);
        if (room.getOccupiedBeds() >= room.getCapacity()) {
            room.setStatus(HostelRoom.RoomStatus.FULL);
        }
        hostelRoomRepository.save(room);

        // Update hostel occupancy
        Hostel hostel = getHostelById(dto.getHostelId());
        hostel.setOccupiedBeds(hostel.getOccupiedBeds() + 1);
        hostel.setUpdatedAt(LocalDateTime.now());
        hostelRepository.save(hostel);

        HostelAllocation allocation = HostelAllocation.builder()
                .id(UUID.randomUUID().toString())
                .studentId(dto.getStudentId())
                .studentName(dto.getStudentName())
                .hostelId(dto.getHostelId())
                .hostelName(dto.getHostelName())
                .roomId(dto.getRoomId())
                .roomNumber(dto.getRoomNumber())
                .bedNumber(dto.getBedNumber())
                .academicYear(dto.getAcademicYear())
                .semester(dto.getSemester())
                .allocationDate(LocalDate.now())
                .status(HostelAllocation.AllocationStatus.ACTIVE)
                .hostelFee(dto.getHostelFee())
                .paymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : HostelAllocation.PaymentStatus.PENDING)
                .build();
        return hostelAllocationRepository.save(allocation);
    }

    public List<HostelAllocation> getStudentAllocation(String studentId) {
        return hostelAllocationRepository.findByStudentId(studentId);
    }

    public List<HostelAllocation> getAllocationsByHostel(String hostelId) {
        return hostelAllocationRepository.findByHostelId(hostelId);
    }

    public HostelAllocation vacateStudent(String allocationId) {
        HostelAllocation allocation = hostelAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new RuntimeException("Allocation not found with id: " + allocationId));
        allocation.setStatus(HostelAllocation.AllocationStatus.VACATED);
        allocation.setVacatingDate(LocalDate.now());

        // Update room occupancy
        HostelRoom room = hostelRoomRepository.findById(allocation.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setOccupiedBeds(Math.max(0, room.getOccupiedBeds() - 1));
        if (room.getOccupiedBeds() < room.getCapacity() && room.getStatus() == HostelRoom.RoomStatus.FULL) {
            room.setStatus(HostelRoom.RoomStatus.AVAILABLE);
        }
        hostelRoomRepository.save(room);

        // Update hostel occupancy
        Hostel hostel = getHostelById(allocation.getHostelId());
        hostel.setOccupiedBeds(Math.max(0, hostel.getOccupiedBeds() - 1));
        hostel.setUpdatedAt(LocalDateTime.now());
        hostelRepository.save(hostel);

        return hostelAllocationRepository.save(allocation);
    }

    public HostelAllocation transferStudent(String allocationId, HostelAllocationDTO newAllocationDto) {
        log.info("Transferring allocation {} to new room", allocationId);
        // Vacate current allocation
        HostelAllocation oldAllocation = vacateStudent(allocationId);
        oldAllocation.setStatus(HostelAllocation.AllocationStatus.TRANSFERRED);
        hostelAllocationRepository.save(oldAllocation);

        // Create new allocation
        newAllocationDto.setStudentId(oldAllocation.getStudentId());
        newAllocationDto.setStudentName(oldAllocation.getStudentName());
        newAllocationDto.setAcademicYear(oldAllocation.getAcademicYear());
        newAllocationDto.setSemester(oldAllocation.getSemester());
        return allocateStudent(newAllocationDto);
    }

    // ==================== MAINTENANCE MANAGEMENT ====================

    public HostelMaintenanceRequest createMaintenanceRequest(MaintenanceRequestDTO dto) {
        log.info("Creating maintenance request for room {} in hostel {}", dto.getRoomId(), dto.getHostelId());
        HostelMaintenanceRequest request = HostelMaintenanceRequest.builder()
                .id(UUID.randomUUID().toString())
                .roomId(dto.getRoomId())
                .roomNumber(dto.getRoomNumber())
                .hostelId(dto.getHostelId())
                .requestedBy(dto.getRequestedBy())
                .requestedByName(dto.getRequestedByName())
                .issueType(dto.getIssueType())
                .description(dto.getDescription())
                .priority(dto.getPriority() != null ? dto.getPriority() : HostelMaintenanceRequest.MaintenancePriority.MEDIUM)
                .status(HostelMaintenanceRequest.MaintenanceStatus.PENDING)
                .assignedTo(dto.getAssignedTo())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return hostelMaintenanceRequestRepository.save(request);
    }

    public List<HostelMaintenanceRequest> getMaintenanceByHostel(String hostelId) {
        return hostelMaintenanceRequestRepository.findByHostelId(hostelId);
    }

    public HostelMaintenanceRequest updateMaintenanceStatus(String id, HostelMaintenanceRequest.MaintenanceStatus status) {
        HostelMaintenanceRequest request = hostelMaintenanceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found with id: " + id));
        request.setStatus(status);
        request.setUpdatedAt(LocalDateTime.now());
        if (status == HostelMaintenanceRequest.MaintenanceStatus.COMPLETED) {
            request.setResolvedAt(LocalDateTime.now());
        }
        return hostelMaintenanceRequestRepository.save(request);
    }

    // ==================== STATISTICS ====================

    public HostelStatsDTO getHostelStatistics() {
        List<Hostel> allHostels = hostelRepository.findAll();
        List<HostelRoom> allRooms = hostelRoomRepository.findAll();
        List<HostelAllocation> allAllocations = hostelAllocationRepository.findAll();
        List<HostelMaintenanceRequest> allMaintenance = hostelMaintenanceRequestRepository.findAll();

        long activeHostels = allHostels.stream().filter(Hostel::isActive).count();
        long availableRooms = allRooms.stream()
                .filter(r -> r.getStatus() == HostelRoom.RoomStatus.AVAILABLE).count();
        long totalCapacity = allRooms.stream().mapToLong(HostelRoom::getCapacity).sum();
        long occupiedBeds = allRooms.stream().mapToLong(HostelRoom::getOccupiedBeds).sum();
        long activeAllocations = allAllocations.stream()
                .filter(a -> a.getStatus() == HostelAllocation.AllocationStatus.ACTIVE).count();
        long pendingMaintenance = allMaintenance.stream()
                .filter(m -> m.getStatus() == HostelMaintenanceRequest.MaintenanceStatus.PENDING).count();
        double occupancyRate = totalCapacity > 0 ? (double) occupiedBeds / totalCapacity * 100 : 0;

        return HostelStatsDTO.builder()
                .totalHostels(allHostels.size())
                .activeHostels(activeHostels)
                .totalRooms(allRooms.size())
                .availableRooms(availableRooms)
                .totalCapacity(totalCapacity)
                .occupiedBeds(occupiedBeds)
                .totalAllocations(allAllocations.size())
                .activeAllocations(activeAllocations)
                .pendingMaintenanceRequests(pendingMaintenance)
                .occupancyRate(Math.round(occupancyRate * 100.0) / 100.0)
                .build();
    }
}
