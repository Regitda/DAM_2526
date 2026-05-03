package com.da.dg2526.api.models.dto.lendingEntity;

import java.time.LocalDate;

public record LendingReturnDTO(LocalDate lendingDate, LocalDate returnDate, String borrowerCode, String bookTitle) {
}
