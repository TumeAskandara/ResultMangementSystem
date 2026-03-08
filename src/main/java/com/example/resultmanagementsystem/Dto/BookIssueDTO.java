package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.model.BookIssue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for issuing a book")
public class BookIssueDTO {

    @Schema(description = "Book ID", example = "BOOK001")
    private String bookId;

    @Schema(description = "Borrower ID", example = "STU001")
    private String borrowerId;

    @Schema(description = "Borrower name", example = "John Doe")
    private String borrowerName;

    @Schema(description = "Borrower type", example = "STUDENT")
    private BookIssue.BorrowerType borrowerType;

    @Schema(description = "Due date for return", example = "2024-04-01")
    private LocalDate dueDate;
}
