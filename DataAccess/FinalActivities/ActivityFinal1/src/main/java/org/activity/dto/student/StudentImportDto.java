package org.activity.dto.student;

public record StudentImportDto(
        String idCard,
        String firstName,
        String lastName,
        String email,
        String phone
) {}