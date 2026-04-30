package com.backend.dto.student;

import jakarta.validation.constraints.*;

public record StudentImportDto(

        @NotBlank(message = "Id card can't be blank.")
        @Size(max = 8, message = "Id card is above 8 characters.")
        String idCard,

        @NotBlank(message = "Id card can't be blank.")
        @Size(max = 50, message = "First name over 100 characters.")
        String firstName,

        @NotBlank(message = "Last name can't be blank.")
        @Size(max = 100, message = "Last name over 100 characters.")
        String lastName,

        @NotBlank(message = "Email can't be blank.")
        @Email(message = "Email invalid.")
        @Size(max = 100, message = "Email too long")
        String email,

        @Pattern(
                regexp = "^\\+?[0-9]{7,15}$",
                message = "Phone must contain 7–15 digits and optional leading +"
        )
        String phone
) {}