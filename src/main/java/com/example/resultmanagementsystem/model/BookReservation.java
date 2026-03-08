package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "book_reservations")
public class BookReservation {

    @Id
    private String id = UUID.randomUUID().toString();

    private String bookId;
    private String bookTitle;
    private String reservedBy;
    private String reservedByName;
    private LocalDateTime reservationDate;
    private LocalDateTime expiryDate;
    private ReservationStatus status;

    public enum ReservationStatus {
        ACTIVE, FULFILLED, EXPIRED, CANCELLED
    }
}
