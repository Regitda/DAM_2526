package com.da.dg2526.api.models.dto.bookEntity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookNewInputDTO(

        @NotBlank(message = "ISBN is required")
        @Size(max = 13, message = "ISBN must be at most 13 characters")
        String isbn,

        @NotBlank(message = "Title is required")
        @Size(max = 90, message = "Title must be at most 90 characters")
        String title,

        @NotNull(message = "Copies are required")
        @Min(value = 0, message = "Copies cannot be negative")
        Integer copies,

        @Size(max = 255, message = "Outline must be at most 255 characters")
        String outline,

        @Size(max = 60, message = "Publisher must be at most 60 characters")
        String publisher,

        //@NotBlank(message = "Category is required") considering example having this as null I will just set everything to other.
        @Size(max = 8, message = "Category must be at most 8 characters")
        String category

) {}
