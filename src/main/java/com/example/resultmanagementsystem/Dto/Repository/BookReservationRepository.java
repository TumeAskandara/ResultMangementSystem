package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.BookReservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReservationRepository extends MongoRepository<BookReservation, String> {

    List<BookReservation> findByBookIdAndStatus(String bookId, BookReservation.ReservationStatus status);

    List<BookReservation> findByReservedByAndStatus(String reservedBy, BookReservation.ReservationStatus status);
}
