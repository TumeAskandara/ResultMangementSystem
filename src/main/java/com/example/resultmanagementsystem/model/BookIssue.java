package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "book_issues")
public class BookIssue {

    @Id
    private String id = UUID.randomUUID().toString();

    private String bookId;
    private String bookTitle;
    private String borrowerId;
    private String borrowerName;
    private BorrowerType borrowerType;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private IssueStatus status;
    private double fineAmount;
    private FineStatus fineStatus;
    private String issuedBy;
    private String returnedTo;
    private String remarks;

    public enum BorrowerType {
        STUDENT, TEACHER, STAFF
    }

    public enum IssueStatus {
        ISSUED, RETURNED, OVERDUE, LOST
    }

    public enum FineStatus {
        PENDING, PAID, WAIVED
    }
}
