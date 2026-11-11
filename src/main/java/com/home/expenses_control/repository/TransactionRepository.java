package com.home.expenses_control.repository;

import com.home.expenses_control.model.Transaction;
import com.home.expenses_control.dto.response.CategoryBreakdownResponseDto;
import com.home.expenses_control.dto.response.DailyBreakdownResponseDto;
import com.home.expenses_control.dto.response.SummaryTotalsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    Transaction saveTransaction(Transaction transaction);
    Transaction getTransactionById(UUID transactionId);
    Page<Transaction> getAllTransactions(Specification<Transaction> spec, Integer page, Integer pageSize);
    SummaryTotalsDto getSummaryTotalsByDates(LocalDate from, LocalDate to);
    List<CategoryBreakdownResponseDto> getCategoryBreakdownByDates(LocalDate from, LocalDate to);
    List<DailyBreakdownResponseDto> getDailyBreakdownByDates(LocalDate from, LocalDate to);
}
