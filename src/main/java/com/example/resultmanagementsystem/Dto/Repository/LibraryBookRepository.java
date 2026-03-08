package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryBookRepository extends MongoRepository<LibraryBook, String> {

    Optional<LibraryBook> findByIsbn(String isbn);

    Page<LibraryBook> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<LibraryBook> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    Page<LibraryBook> findByCategory(String category, Pageable pageable);

    Page<LibraryBook> findByCategoryAndIsActive(String category, boolean isActive, Pageable pageable);

    long countByIsActive(boolean isActive);
}
