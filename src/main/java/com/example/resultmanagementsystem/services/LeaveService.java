package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.LeaveBalanceDTO;
import com.example.resultmanagementsystem.Dto.LeaveRequestDTO;
import com.example.resultmanagementsystem.Dto.LeaveReviewDTO;
import com.example.resultmanagementsystem.Dto.Repository.LeaveBalanceRepository;
import com.example.resultmanagementsystem.Dto.Repository.LeaveRequestRepository;
import com.example.resultmanagementsystem.Dto.Repository.StaffRepository;
import com.example.resultmanagementsystem.model.LeaveBalance;
import com.example.resultmanagementsystem.model.LeaveRequest;
import com.example.resultmanagementsystem.model.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final StaffRepository staffRepository;

    public LeaveRequest applyLeave(LeaveRequestDTO leaveRequestDTO, String staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

        int totalDays = (int) ChronoUnit.DAYS.between(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate()) + 1;

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .staffId(staffId)
                .staffName(staff.getFirstName() + " " + staff.getLastName())
                .leaveType(leaveRequestDTO.getLeaveType())
                .startDate(leaveRequestDTO.getStartDate())
                .endDate(leaveRequestDTO.getEndDate())
                .totalDays(totalDays)
                .reason(leaveRequestDTO.getReason())
                .status(LeaveRequest.LeaveStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .attachmentUrl(leaveRequestDTO.getAttachmentUrl())
                .build();

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest getLeaveById(String id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with ID: " + id));
    }

    public List<LeaveRequest> getLeavesByStaff(String staffId) {
        return leaveRequestRepository.findByStaffId(staffId);
    }

    public Page<LeaveRequest> getPendingLeaves(Pageable pageable) {
        return leaveRequestRepository.findByStatus(LeaveRequest.LeaveStatus.PENDING, pageable);
    }

    public LeaveRequest reviewLeave(String id, LeaveReviewDTO leaveReviewDTO) {
        LeaveRequest leaveRequest = getLeaveById(id);

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Leave request is not in PENDING status. Current status: " + leaveRequest.getStatus());
        }

        leaveRequest.setStatus(leaveReviewDTO.getStatus());
        leaveRequest.setApproverRemarks(leaveReviewDTO.getApproverRemarks());
        leaveRequest.setReviewedAt(LocalDateTime.now());

        if (leaveReviewDTO.getStatus() == LeaveRequest.LeaveStatus.APPROVED) {
            updateLeaveBalanceOnApproval(leaveRequest);
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest cancelLeave(String id, String staffId) {
        LeaveRequest leaveRequest = getLeaveById(id);

        if (!leaveRequest.getStaffId().equals(staffId)) {
            throw new RuntimeException("You can only cancel your own leave requests.");
        }

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Only PENDING leave requests can be cancelled. Current status: " + leaveRequest.getStatus());
        }

        leaveRequest.setStatus(LeaveRequest.LeaveStatus.CANCELLED);
        leaveRequest.setReviewedAt(LocalDateTime.now());
        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveBalance getLeaveBalance(String staffId, String academicYear) {
        return leaveBalanceRepository.findByStaffIdAndAcademicYear(staffId, academicYear)
                .orElseThrow(() -> new RuntimeException("Leave balance not found for staff: " + staffId + " and year: " + academicYear));
    }

    public LeaveBalance initializeLeaveBalance(String staffId, String academicYear) {
        leaveBalanceRepository.findByStaffIdAndAcademicYear(staffId, academicYear)
                .ifPresent(lb -> {
                    throw new RuntimeException("Leave balance already exists for staff: " + staffId + " and year: " + academicYear);
                });

        staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

        LeaveBalance leaveBalance = LeaveBalance.builder()
                .staffId(staffId)
                .academicYear(academicYear)
                .sickLeave(12)
                .casualLeave(10)
                .annualLeave(20)
                .maternityLeave(90)
                .paternityLeave(14)
                .usedSickLeave(0)
                .usedCasualLeave(0)
                .usedAnnualLeave(0)
                .usedMaternityLeave(0)
                .usedPaternityLeave(0)
                .build();

        return leaveBalanceRepository.save(leaveBalance);
    }

    private void updateLeaveBalanceOnApproval(LeaveRequest leaveRequest) {
        // Determine academic year from the leave start date
        int year = leaveRequest.getStartDate().getYear();
        int month = leaveRequest.getStartDate().getMonthValue();
        String academicYear = month >= 9 ? year + "-" + (year + 1) : (year - 1) + "-" + year;

        LeaveBalance balance = leaveBalanceRepository.findByStaffIdAndAcademicYear(leaveRequest.getStaffId(), academicYear)
                .orElse(null);

        if (balance == null) {
            return; // No balance record to update
        }

        int days = leaveRequest.getTotalDays();
        switch (leaveRequest.getLeaveType()) {
            case SICK -> balance.setUsedSickLeave(balance.getUsedSickLeave() + days);
            case CASUAL -> balance.setUsedCasualLeave(balance.getUsedCasualLeave() + days);
            case ANNUAL -> balance.setUsedAnnualLeave(balance.getUsedAnnualLeave() + days);
            case MATERNITY -> balance.setUsedMaternityLeave(balance.getUsedMaternityLeave() + days);
            case PATERNITY -> balance.setUsedPaternityLeave(balance.getUsedPaternityLeave() + days);
            default -> { /* UNPAID, STUDY, COMPASSIONATE - no balance tracking */ }
        }

        leaveBalanceRepository.save(balance);
    }
}
