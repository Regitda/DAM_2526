package com.da.dg2526.api.models.dto.bookEntity;

import com.da.dg2526.api.models.dto.reserveEntity.ReserveResultDTO;
import com.da.dg2526.api.models.enums.Status;

public record BookReserveResponseDTO(Status status, String error, ReserveResultDTO reserveResultDTO) {
}
