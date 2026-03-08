package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.BookIssueDTO;
import com.example.resultmanagementsystem.Dto.BookReservationDTO;
import com.example.resultmanagementsystem.Dto.BookReturnDTO;
import com.example.resultmanagementsystem.Dto.LibraryBookDTO;
import com.example.resultmanagementsystem.Dto.LibraryStatsDTO;
import com.example.resultmanagementsystem.model.BookIssue;
import com.example.resultmanagementsystem.model.BookReservation;
import com.example.resultmanagementsystem.model.LibraryBook;
import com.example.resultmanagementsystem.services.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Library Management", description = "APIs for managing library books, book issues, returns, reservations, and library statistics.")
public class LibraryController {

    private final LibraryService libraryService;

    // ==================== Book Management ====================

    @PostMapping("/admin/books")
    @Operation(
            summary = "Add a new book to the library",
            description = "Adds a new book record to the library catalog with all necessary details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryBook.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LibraryBook> addBook(
            @Parameter(description = "Book details", required = true)
            @RequestBody LibraryBookDTO libraryBookDTO) {
        LibraryBook book = libraryService.addBook(libraryBookDTO);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @GetMapping("/books/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Retrieves a specific book's complete information using its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryBook.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LibraryBook> getBookById(
            @Parameter(description = "Book ID", required = true)
            @PathVariable String id) {
        LibraryBook book = libraryService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/books/search")
    @Operation(
            summary = "Search books",
            description = "Searches for books by title or author name. Returns paginated results."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<LibraryBook>> searchBooks(
            @Parameter(description = "Search query (title or author)", required = true, example = "Algorithms")
            @RequestParam String query,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LibraryBook> books = libraryService.searchBooks(query, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/category/{category}")
    @Operation(
            summary = "Get books by category",
            description = "Retrieves a paginated list of active books in a specific category."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<LibraryBook>> getBooksByCategory(
            @Parameter(description = "Book category", required = true, example = "Computer Science")
            @PathVariable String category,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LibraryBook> books = libraryService.getBooksByCategory(category, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/admin/books/{id}")
    @Operation(
            summary = "Update book details",
            description = "Updates an existing book's information. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryBook.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LibraryBook> updateBook(
            @Parameter(description = "Book ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated book details", required = true)
            @RequestBody LibraryBookDTO libraryBookDTO) {
        LibraryBook updatedBook = libraryService.updateBook(id, libraryBookDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    // ==================== Book Issue & Return ====================

    @PostMapping("/issue")
    @Operation(
            summary = "Issue a book",
            description = "Issues a book to a borrower (student, teacher, or staff). Decreases the available copies count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book issued successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookIssue.class))),
            @ApiResponse(responseCode = "400", description = "No available copies or invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<BookIssue> issueBook(
            @Parameter(description = "Book issue details", required = true)
            @RequestBody BookIssueDTO bookIssueDTO,
            @Parameter(description = "ID of the person issuing the book", required = true)
            @RequestParam String issuedBy) {
        BookIssue bookIssue = libraryService.issueBook(bookIssueDTO, issuedBy);
        return new ResponseEntity<>(bookIssue, HttpStatus.CREATED);
    }

    @PatchMapping("/return/{issueId}")
    @Operation(
            summary = "Return a book",
            description = "Processes the return of an issued book. Calculates fine if overdue and increases available copies count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookIssue.class))),
            @ApiResponse(responseCode = "400", description = "Book already returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Issue record not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<BookIssue> returnBook(
            @Parameter(description = "Book issue ID", required = true)
            @PathVariable String issueId,
            @Parameter(description = "Return details")
            @RequestBody(required = false) BookReturnDTO bookReturnDTO,
            @Parameter(description = "ID of the person accepting the return", required = true)
            @RequestParam String returnedTo) {
        BookIssue bookIssue = libraryService.returnBook(issueId, bookReturnDTO, returnedTo);
        return new ResponseEntity<>(bookIssue, HttpStatus.OK);
    }

    @GetMapping("/issues/borrower/{borrowerId}")
    @Operation(
            summary = "Get issues by borrower",
            description = "Retrieves all book issue records for a specific borrower."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Issue records retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<BookIssue>> getIssuesByBorrower(
            @Parameter(description = "Borrower ID", required = true)
            @PathVariable String borrowerId) {
        List<BookIssue> issues = libraryService.getIssuesByBorrower(borrowerId);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping("/issues/overdue")
    @Operation(
            summary = "Get overdue books",
            description = "Retrieves all currently overdue book issues."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue books retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<BookIssue>> getOverdueBooks() {
        List<BookIssue> overdueBooks = libraryService.getOverdueBooks();
        return new ResponseEntity<>(overdueBooks, HttpStatus.OK);
    }

    @GetMapping("/issues/{issueId}/fine")
    @Operation(
            summary = "Calculate fine for a book issue",
            description = "Calculates the fine amount for a specific book issue based on overdue days."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine calculated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Issue record not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> calculateFine(
            @Parameter(description = "Book issue ID", required = true)
            @PathVariable String issueId) {
        double fine = libraryService.calculateFine(issueId);
        Map<String, Object> response = new HashMap<>();
        response.put("issueId", issueId);
        response.put("fineAmount", fine);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== Book Reservation ====================

    @PostMapping("/reserve")
    @Operation(
            summary = "Reserve a book",
            description = "Creates a reservation for a book. The reservation will be active for 3 days."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book reserved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookReservation.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<BookReservation> reserveBook(
            @Parameter(description = "Reservation details", required = true)
            @RequestBody BookReservationDTO bookReservationDTO) {
        BookReservation reservation = libraryService.reserveBook(bookReservationDTO);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @GetMapping("/reservations/book/{bookId}")
    @Operation(
            summary = "Get active reservations for a book",
            description = "Retrieves all active reservations for a specific book."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<BookReservation>> getReservationsByBook(
            @Parameter(description = "Book ID", required = true)
            @PathVariable String bookId) {
        List<BookReservation> reservations = libraryService.getReservationsByBook(bookId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @DeleteMapping("/reservations/{id}")
    @Operation(
            summary = "Cancel a reservation",
            description = "Cancels an active book reservation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Reservation is not in ACTIVE status",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> cancelReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable String id) {
        libraryService.cancelReservation(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reservation cancelled successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== Statistics ====================

    @GetMapping("/statistics")
    @Operation(
            summary = "Get library statistics",
            description = "Retrieves overall library statistics including total books, issued books, overdue counts, and pending fines."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Library statistics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryStatsDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<LibraryStatsDTO> getLibraryStatistics() {
        LibraryStatsDTO stats = libraryService.getLibraryStatistics();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operation Failed");
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
