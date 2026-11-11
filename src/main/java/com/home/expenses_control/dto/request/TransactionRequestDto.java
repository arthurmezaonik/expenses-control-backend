package com.home.expenses_control.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home.expenses_control.enums.Type;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequestDto(
    @Valid
    @NotNull(message = "Transaction type can not be null.")
    Type type,
    @NotNull(message = "Transaction amount can not be null.")
    @Positive(message = "Transaction amount must be a positive number.")
    @Digits(integer = 10, fraction = 2)
    BigDecimal amount,
    @NotNull(message = "Transaction date can not be null.")
    LocalDate date,
    @Valid
    @NotNull(message = "Transaction category ID can not be null.")
    @JsonProperty("category_id")
    UUID categoryId,
    @Nullable
    String note
) {
}
