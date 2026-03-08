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
@Schema(description = "Data transfer object for Library Book")
public class LibraryBookDTO {

    @Schema(description = "ISBN number", example = "978-3-16-148410-0")
    private String isbn;

    @Schema(description = "Book title", example = "Introduction to Algorithms")
    private String title;

    @Schema(description = "Author name", example = "Thomas H. Cormen")
    private String author;

    @Schema(description = "Publisher name", example = "MIT Press")
    private String publisher;

    @Schema(description = "Year of publication", example = "2009")
    private int publishYear;

    @Schema(description = "Book category", example = "Computer Science")
    private String category;

    @Schema(description = "Book genre", example = "Textbook")
    private String genre;

    @Schema(description = "Total number of copies", example = "5")
    private int totalCopies;

    @Schema(description = "Shelf location", example = "A-12-3")
    private String shelfLocation;

    @Schema(description = "Book description")
    private String description;

    @Schema(description = "Cover image URL")
    private String coverImageUrl;
}
