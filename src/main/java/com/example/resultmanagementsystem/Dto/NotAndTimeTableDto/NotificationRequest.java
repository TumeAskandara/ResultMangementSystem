package com.example.resultmanagementsystem.Dto.NotAndTimeTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationRequest {
    private String title;
    private String message;
    private String type;
    private String priority;
    private List<String> recipientIds;
    private String recipientType;
    private String timetableId;
    private LocalDateTime scheduledDate;
}