package com.home.expenses_control.dto.response;

import com.home.expenses_control.enums.Status;
import com.home.expenses_control.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(
    UUID id,
    Type type,
    BigDecimal amount,
    LocalDate date,
    CategoryResponseDto category,
    String note,
    Status status
) {
}
