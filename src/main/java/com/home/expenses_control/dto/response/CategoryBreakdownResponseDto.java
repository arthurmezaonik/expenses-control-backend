package com.home.expenses_control.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CategoryBreakdownResponseDto(
    @JsonProperty("name")
    String categoryName,
    @JsonProperty("total_income")
    BigDecimal totalIncome,
    @JsonProperty("total_expenses")
    BigDecimal totalExpenses,
    BigDecimal net
) {
}
