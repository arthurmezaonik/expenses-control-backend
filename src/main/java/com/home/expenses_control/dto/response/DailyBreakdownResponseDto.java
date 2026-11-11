package com.home.expenses_control.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyBreakdownResponseDto(
    LocalDate date,
    @JsonProperty("total_income")
    BigDecimal totalIncome,
    @JsonProperty("total_expenses")
    BigDecimal totalExpenses,
    BigDecimal net
) {
}
