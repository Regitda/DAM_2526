package com.da.dg2526.api.models.dto.bookEntity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookNewInputDTO {

    @NotBlank(message = "ISBN is required")
    @Size(max = 13, message = "ISBN must be at most 13 characters")
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(max = 90, message = "Title must be at most 90 characters")
    private String title;

    @Min(value = 0, message = "Copies cannot be negative")
    private Integer copies;

    @Size(max = 255, message = "Outline must be at most 255 characters")
    private String outline;

    @Size(max = 60, message = "Publisher must be at most 60 characters")
    private String publisher;

    @NotBlank(message = "Category is required")
    @Size(max = 8, message = "Category must be at most 8 characters")
    private String category;

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getOutline() {
        return outline;
    }

    public Integer getCopies() {
        return copies;
    }

    public String getTitle() {
        return title;
    }

}
