package com.da.dg2526.api.models.dto.bookEntity;

import com.da.dg2526.api.models.dto.lendingEntity.LendingReturnDTO;
import com.da.dg2526.api.models.enums.Status;

public record BookLendingResponseDTO(Status status, String error, LendingReturnDTO lendingReturnDTO, Boolean canReserve)  {
}
