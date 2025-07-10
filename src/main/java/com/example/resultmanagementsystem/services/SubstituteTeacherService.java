package com.example.resultmanagementsystem.services;


import com.example.resultmanagementsystem.Dto.Repository.SubstituteRequestRepository;
import com.example.resultmanagementsystem.Dto.Repository.TimetableRepository;
import com.example.resultmanagementsystem.model.SubstituteRequest;
import com.example.resultmanagementsystem.model.Timetable;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubstituteTeacherService {

    private final SubstituteRequestRepository substituteRequestRepository;
    private final TimetableRepository timetableRepository;
    private final NotificationService notificationService;

    public List<SubstituteRequest> getAllSubstituteRequests() {
        return substituteRequestRepository.findAll();
    }

    public Optional<SubstituteRequest> getSubstituteRequestById(String requestId) {
        return substituteRequestRepository.findById(requestId);
    }

    public List<SubstituteRequest> getSubstituteRequestsByTeacher(String teacherId) {
        return substituteRequestRepository.findByOriginalTeacherId(teacherId);
    }

    public List<SubstituteRequest> getSubstituteRequestsByStatus(String status) {
        return substituteRequestRepository.findByStatus(status);
    }

    public SubstituteRequest createSubstituteRequest(SubstituteRequest request) {
        // Validate that substitute teacher is available
        if (hasSubstituteConflict(request)) {
            throw new RuntimeException("Substitute teacher has a conflicting schedule");
        }

        request.setRequestDate(LocalDate.now());
        return substituteRequestRepository.save(request);
    }

    public SubstituteRequest approveSubstituteRequest(String requestId, String approvedBy) {
        return substituteRequestRepository.findById(requestId)
                .map(request -> {
                    request.setStatus("APPROVED");
                    request.setApprovedBy(approvedBy);
                    request.setUpdatedDate(LocalDateTime.now());

                    // Update the timetable with substitute teacher
                    updateTimetableWithSubstitute(request);

                    return substituteRequestRepository.save(request);
                })
                .orElseThrow(() -> new RuntimeException("Substitute request not found"));
    }

    public SubstituteRequest rejectSubstituteRequest(String requestId, String rejectedBy, String comments) {
        return substituteRequestRepository.findById(requestId)
                .map(request -> {
                    request.setStatus("REJECTED");
                    request.setApprovedBy(rejectedBy);
                    request.setComments(comments);
                    request.setUpdatedDate(LocalDateTime.now());

                    return substituteRequestRepository.save(request);
                })
                .orElseThrow(() -> new RuntimeException("Substitute request not found"));
    }

    private void updateTimetableWithSubstitute(SubstituteRequest request) {
        timetableRepository.findById(request.getTimetableId())
                .ifPresent(timetable -> {
                    timetable.setSubstituteTeacherId(request.getSubstituteTeacherId());
                    timetable.setIsSubstituted(true);
                    timetable.setSubstituteReason(request.getReason());
                    timetable.setSubstitutionDate(LocalDate.now());
                    timetable.setStatus("SUBSTITUTED");
                    timetable.setUpdatedDate(LocalDate.now());

                    timetableRepository.save(timetable);

                    // Send substitution notification
                    notificationService.createSubstitutionNotification(
                            timetable,
                            request.getSubstituteTeacherId(),
                            request.getReason()
                    );
                });
    }

    private boolean hasSubstituteConflict(SubstituteRequest request) {
        return timetableRepository.findById(request.getTimetableId())
                .map(timetable -> {
                    List<Timetable> conflicts = timetableRepository.findConflictingSchedules(
                            request.getSubstituteTeacherId(),
                            timetable.getDayOfWeek(),
                            timetable.getStartTime(),
                            timetable.getEndTime()
                    );
                    return !conflicts.isEmpty();
                })
                .orElse(false);
    }

    public void completeSubstitution(String requestId) {
        substituteRequestRepository.findById(requestId)
                .ifPresent(request -> {
                    request.setStatus("COMPLETED");
                    request.setUpdatedDate(LocalDateTime.now());
                    substituteRequestRepository.save(request);
                });
    }
}