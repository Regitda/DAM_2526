package com.da.dg2526.api.models.dto.reserveEntity;

import java.time.LocalDate;

public record ReserveResultDTO(LocalDate reservationDate, String borrower, Integer amountOfReservationsOnThatBook) {
}
