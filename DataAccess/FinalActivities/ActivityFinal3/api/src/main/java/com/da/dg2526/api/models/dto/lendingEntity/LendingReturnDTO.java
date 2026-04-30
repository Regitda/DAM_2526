package com.da.dg2526.api.models.dto.lendingEntity;

import java.util.Date;

public record LendingReturnDTO(Integer id, Date lendingDate, Date returnDate, String borrower) {
}
