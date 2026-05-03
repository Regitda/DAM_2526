package com.da.dg2526.api.models.dto.bookEntity;

import com.da.dg2526.api.models.enums.Status;

public record BookReturnResponseDTO(Status status, String message, Boolean lateReturn) {
}
