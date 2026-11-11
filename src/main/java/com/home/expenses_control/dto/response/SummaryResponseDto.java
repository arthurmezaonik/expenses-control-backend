package com.home.expenses_control.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SummaryResponseDto(
    LocalDate from,
    LocalDate to,
    String currency,
    @JsonProperty("total_income")
    BigDecimal totalIncome,
    @JsonProperty("total_expenses")
    BigDecimal totalExpenses,
    BigDecimal net,
    @JsonProperty("category_breakdown")
    List<CategoryBreakdownResponseDto> categoryBreakdown,
    @JsonProperty("daily_breakdown")
    List<DailyBreakdownResponseDto> dailyBreakdown,
    List<TransactionResponseDto> transactions,
    PaginationResponseDto pagination
) {
}
