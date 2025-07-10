package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecurrencePattern {
    private RecurrenceType type;
    private int interval; // every N days/weeks/months
    private LocalDate endDate;
    private Integer occurrences; // number of occurrences

    public enum RecurrenceType {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }
}