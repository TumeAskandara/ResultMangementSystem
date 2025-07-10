package com.example.resultmanagementsystem.Dto.NotAndTimeTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimetableDashboard {
    private Long totalTimetables;
    private Long activeTimetables;
    private Long substitutedTimetables;
    private Long pendingSubstitutions;
    private Long todaysClasses;
    private Long conflictingSchedules;
    private NotificationSummary notificationSummary;
}