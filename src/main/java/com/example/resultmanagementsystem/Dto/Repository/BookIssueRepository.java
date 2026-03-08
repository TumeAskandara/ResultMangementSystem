package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.BookIssue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookIssueRepository extends MongoRepository<BookIssue, String> {

    List<BookIssue> findByBorrowerId(String borrowerId);

    List<BookIssue> findByBookId(String bookId);

    List<BookIssue> findByStatus(BookIssue.IssueStatus status);

    List<BookIssue> findByBorrowerIdAndStatus(String borrowerId, BookIssue.IssueStatus status);

    List<BookIssue> findByDueDateBeforeAndStatus(LocalDate dueDate, BookIssue.IssueStatus status);
}
