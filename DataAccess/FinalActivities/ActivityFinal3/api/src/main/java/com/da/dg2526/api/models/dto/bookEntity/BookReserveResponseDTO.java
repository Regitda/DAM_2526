package com.da.dg2526.api.models.dto.bookEntity;

import com.da.dg2526.api.models.dto.reserveEntity.ReserveResultDTO;

public record BookReserveResponseDTO(String status, String error, ReserveResultDTO reserveResultDTO) {
}
