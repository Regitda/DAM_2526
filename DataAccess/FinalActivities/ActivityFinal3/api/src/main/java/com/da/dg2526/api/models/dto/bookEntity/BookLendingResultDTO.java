package com.da.dg2526.api.models.dto.bookEntity;

import com.da.dg2526.api.models.dto.lendingEntity.LendingReturnDTO;

public record BookLendingResultDTO(String status, String error, LendingReturnDTO lendingReturnDTO, Boolean canReserve) {
}
