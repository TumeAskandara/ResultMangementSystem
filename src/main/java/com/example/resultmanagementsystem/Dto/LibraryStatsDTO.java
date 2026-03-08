package com.example.resultmanagementsystem.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Library Statistics")
public class LibraryStatsDTO {

    @Schema(description = "Total number of books in the library")
    private long totalBooks;

    @Schema(description = "Number of active books")
    private long activeBooks;

    @Schema(description = "Number of currently issued books")
    private long issuedBooks;

    @Schema(description = "Number of overdue books")
    private long overdueBooks;

    @Schema(description = "Number of active reservations")
    private long activeReservations;

    @Schema(description = "Total fines pending")
    private double totalPendingFines;
}
