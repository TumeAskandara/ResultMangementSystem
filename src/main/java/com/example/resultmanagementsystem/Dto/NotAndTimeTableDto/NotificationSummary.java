package com.example.resultmanagementsystem.Dto.NotAndTimeTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationSummary {
    private Long totalNotifications;
    private Long unreadNotifications;
    private Long pendingNotifications;
    private Long todaysReminders;
    private Long substituteAlerts;
    private Long scheduleChanges;
}