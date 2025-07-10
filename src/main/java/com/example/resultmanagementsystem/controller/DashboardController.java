package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.NotAndTimeTableDto.TimetableDashboard;
import com.example.resultmanagementsystem.Dto.NotAndTimeTableDto.NotificationSummary;
import com.example.resultmanagementsystem.services.NotificationService;
import com.example.resultmanagementsystem.services.SubstituteTeacherService;
import com.example.resultmanagementsystem.services.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final TimetableService timetableService;
    private final NotificationService notificationService;
    private final SubstituteTeacherService substituteTeacherService;

    @GetMapping("/summary")
    public ResponseEntity<TimetableDashboard> getDashboardSummary() {
        TimetableDashboard dashboard = new TimetableDashboard();

        // Set timetable statistics
        dashboard.setTotalTimetables((long) timetableService.getAllTimetables().size());
        dashboard.setActiveTimetables((long) timetableService.getTimetablesByStatus("ACTIVE").size());
        dashboard.setSubstitutedTimetables((long) timetableService.getSubstitutedTimetables().size());

        // Set substitute statistics
        dashboard.setPendingSubstitutions((long) substituteTeacherService.getSubstituteRequestsByStatus("PENDING").size());

        // Set notification summary
        NotificationSummary notificationSummary = new NotificationSummary();
        notificationSummary.setTotalNotifications((long) notificationService.getAllNotifications().size());
        dashboard.setNotificationSummary(notificationSummary);

        return ResponseEntity.ok(dashboard);
    }
}