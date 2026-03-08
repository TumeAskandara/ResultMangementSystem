package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.BookIssueDTO;
import com.example.resultmanagementsystem.Dto.BookReservationDTO;
import com.example.resultmanagementsystem.Dto.BookReturnDTO;
import com.example.resultmanagementsystem.Dto.LibraryBookDTO;
import com.example.resultmanagementsystem.Dto.LibraryStatsDTO;
import com.example.resultmanagementsystem.Dto.Repository.BookIssueRepository;
import com.example.resultmanagementsystem.Dto.Repository.BookReservationRepository;
import com.example.resultmanagementsystem.Dto.Repository.LibraryBookRepository;
import com.example.resultmanagementsystem.model.BookIssue;
import com.example.resultmanagementsystem.model.BookReservation;
import com.example.resultmanagementsystem.model.LibraryBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LibraryService {

    private final LibraryBookRepository libraryBookRepository;
    private final BookIssueRepository bookIssueRepository;
    private final BookReservationRepository bookReservationRepository;

    private static final double FINE_PER_DAY = 1.0;

    // ==================== Book Management ====================

    public LibraryBook addBook(LibraryBookDTO dto) {
        LibraryBook book = LibraryBook.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .publishYear(dto.getPublishYear())
                .category(dto.getCategory())
                .genre(dto.getGenre())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getTotalCopies())
                .shelfLocation(dto.getShelfLocation())
                .description(dto.getDescription())
                .coverImageUrl(dto.getCoverImageUrl())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return libraryBookRepository.save(book);
    }

    public LibraryBook getBookById(String id) {
        return libraryBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    public Page<LibraryBook> searchBooks(String query, Pageable pageable) {
        Page<LibraryBook> byTitle = libraryBookRepository.findByTitleContainingIgnoreCase(query, pageable);
        if (byTitle.hasContent()) {
            return byTitle;
        }
        return libraryBookRepository.findByAuthorContainingIgnoreCase(query, pageable);
    }

    public Page<LibraryBook> getBooksByCategory(String category, Pageable pageable) {
        return libraryBookRepository.findByCategoryAndIsActive(category, true, pageable);
    }

    public LibraryBook updateBook(String id, LibraryBookDTO dto) {
        LibraryBook book = getBookById(id);

        if (dto.getIsbn() != null) book.setIsbn(dto.getIsbn());
        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
        if (dto.getPublisher() != null) book.setPublisher(dto.getPublisher());
        if (dto.getPublishYear() > 0) book.setPublishYear(dto.getPublishYear());
        if (dto.getCategory() != null) book.setCategory(dto.getCategory());
        if (dto.getGenre() != null) book.setGenre(dto.getGenre());
        if (dto.getTotalCopies() > 0) {
            int diff = dto.getTotalCopies() - book.getTotalCopies();
            book.setTotalCopies(dto.getTotalCopies());
            book.setAvailableCopies(book.getAvailableCopies() + diff);
        }
        if (dto.getShelfLocation() != null) book.setShelfLocation(dto.getShelfLocation());
        if (dto.getDescription() != null) book.setDescription(dto.getDescription());
        if (dto.getCoverImageUrl() != null) book.setCoverImageUrl(dto.getCoverImageUrl());

        book.setUpdatedAt(LocalDateTime.now());
        return libraryBookRepository.save(book);
    }

    // ==================== Book Issue ====================

    public BookIssue issueBook(BookIssueDTO dto, String issuedBy) {
        LibraryBook book = getBookById(dto.getBookId());

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies of book: " + book.getTitle());
        }

        BookIssue bookIssue = BookIssue.builder()
                .bookId(dto.getBookId())
                .bookTitle(book.getTitle())
                .borrowerId(dto.getBorrowerId())
                .borrowerName(dto.getBorrowerName())
                .borrowerType(dto.getBorrowerType())
                .issueDate(LocalDate.now())
                .dueDate(dto.getDueDate())
                .status(BookIssue.IssueStatus.ISSUED)
                .fineAmount(0.0)
                .fineStatus(BookIssue.FineStatus.PENDING)
                .issuedBy(issuedBy)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        book.setUpdatedAt(LocalDateTime.now());
        libraryBookRepository.save(book);

        return bookIssueRepository.save(bookIssue);
    }

    public BookIssue returnBook(String issueId, BookReturnDTO dto, String returnedTo) {
        BookIssue bookIssue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Book issue record not found with ID: " + issueId));

        if (bookIssue.getStatus() == BookIssue.IssueStatus.RETURNED) {
            throw new RuntimeException("Book has already been returned.");
        }

        bookIssue.setReturnDate(LocalDate.now());
        bookIssue.setStatus(BookIssue.IssueStatus.RETURNED);
        bookIssue.setReturnedTo(returnedTo);
        if (dto != null && dto.getRemarks() != null) {
            bookIssue.setRemarks(dto.getRemarks());
        }

        // Calculate fine if overdue
        if (LocalDate.now().isAfter(bookIssue.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(bookIssue.getDueDate(), LocalDate.now());
            bookIssue.setFineAmount(overdueDays * FINE_PER_DAY);
        }

        // Update book available copies
        LibraryBook book = getBookById(bookIssue.getBookId());
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setUpdatedAt(LocalDateTime.now());
        libraryBookRepository.save(book);

        return bookIssueRepository.save(bookIssue);
    }

    public List<BookIssue> getIssuesByBorrower(String borrowerId) {
        return bookIssueRepository.findByBorrowerId(borrowerId);
    }

    public List<BookIssue> getOverdueBooks() {
        return bookIssueRepository.findByDueDateBeforeAndStatus(LocalDate.now(), BookIssue.IssueStatus.ISSUED);
    }

    public double calculateFine(String issueId) {
        BookIssue bookIssue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Book issue record not found with ID: " + issueId));

        if (bookIssue.getStatus() == BookIssue.IssueStatus.RETURNED) {
            return bookIssue.getFineAmount();
        }

        LocalDate compareDate = bookIssue.getReturnDate() != null ? bookIssue.getReturnDate() : LocalDate.now();
        if (compareDate.isAfter(bookIssue.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(bookIssue.getDueDate(), compareDate);
            return overdueDays * FINE_PER_DAY;
        }

        return 0.0;
    }

    // ==================== Book Reservation ====================

    public BookReservation reserveBook(BookReservationDTO dto) {
        LibraryBook book = getBookById(dto.getBookId());

        BookReservation reservation = BookReservation.builder()
                .bookId(dto.getBookId())
                .bookTitle(book.getTitle())
                .reservedBy(dto.getReservedBy())
                .reservedByName(dto.getReservedByName())
                .reservationDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(3))
                .status(BookReservation.ReservationStatus.ACTIVE)
                .build();

        return bookReservationRepository.save(reservation);
    }

    public List<BookReservation> getReservationsByBook(String bookId) {
        return bookReservationRepository.findByBookIdAndStatus(bookId, BookReservation.ReservationStatus.ACTIVE);
    }

    public void cancelReservation(String id) {
        BookReservation reservation = bookReservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

        if (reservation.getStatus() != BookReservation.ReservationStatus.ACTIVE) {
            throw new RuntimeException("Only ACTIVE reservations can be cancelled.");
        }

        reservation.setStatus(BookReservation.ReservationStatus.CANCELLED);
        bookReservationRepository.save(reservation);
    }

    // ==================== Statistics ====================

    public LibraryStatsDTO getLibraryStatistics() {
        long totalBooks = libraryBookRepository.count();
        long activeBooks = libraryBookRepository.countByIsActive(true);
        long issuedBooks = bookIssueRepository.findByStatus(BookIssue.IssueStatus.ISSUED).size();
        List<BookIssue> overdueBooks = getOverdueBooks();
        long activeReservations = bookReservationRepository
                .findByBookIdAndStatus(null, BookReservation.ReservationStatus.ACTIVE).size();

        double totalPendingFines = bookIssueRepository.findByStatus(BookIssue.IssueStatus.ISSUED).stream()
                .filter(issue -> LocalDate.now().isAfter(issue.getDueDate()))
                .mapToDouble(issue -> ChronoUnit.DAYS.between(issue.getDueDate(), LocalDate.now()) * FINE_PER_DAY)
                .sum();

        return LibraryStatsDTO.builder()
                .totalBooks(totalBooks)
                .activeBooks(activeBooks)
                .issuedBooks(issuedBooks)
                .overdueBooks(overdueBooks.size())
                .activeReservations(activeReservations)
                .totalPendingFines(totalPendingFines)
                .build();
    }
}
