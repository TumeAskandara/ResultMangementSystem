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
@Document(collection = "library_books")
public class LibraryBook {

    @Id
    private String id = UUID.randomUUID().toString();

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int publishYear;
    private String category;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    private String shelfLocation;
    private String description;
    private String coverImageUrl;
    private boolean isActive;
    private String addedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
