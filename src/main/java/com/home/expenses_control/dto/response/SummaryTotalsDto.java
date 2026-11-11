package com.home.expenses_control.dto.response;

import java.math.BigDecimal;

public record SummaryTotalsDto(BigDecimal totalIncome, BigDecimal totalExpenses) {
    public SummaryTotalsDto {
        totalIncome = totalIncome == null ? BigDecimal.ZERO : totalIncome;
        totalExpenses = totalExpenses == null ? BigDecimal.ZERO : totalExpenses;
    }
}
