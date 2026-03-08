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
@Schema(description = "Data transfer object for reserving a book")
public class BookReservationDTO {

    @Schema(description = "Book ID to reserve", example = "BOOK001")
    private String bookId;

    @Schema(description = "User ID making the reservation", example = "STU001")
    private String reservedBy;

    @Schema(description = "Name of the person reserving", example = "John Doe")
    private String reservedByName;
}
