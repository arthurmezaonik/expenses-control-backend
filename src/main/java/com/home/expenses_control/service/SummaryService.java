package com.home.expenses_control.service;

import com.home.expenses_control.dto.response.SummaryResponseDto;

import java.time.LocalDate;

public interface SummaryService {

    SummaryResponseDto getTransactionsSummary(int page, int pageSize, LocalDate from, LocalDate to);
}
