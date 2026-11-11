package com.home.expenses_control.service;

import com.home.expenses_control.dto.response.PaginationResponseDto;
import com.home.expenses_control.dto.response.SummaryResponseDto;
import com.home.expenses_control.dto.response.TransactionResponseDtoPaginated;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.exceptions.InvalidDateException;
import com.home.expenses_control.exceptions.InvalidPageException;
import com.home.expenses_control.exceptions.InvalidPageSizeException;
import com.home.expenses_control.mapper.TransactionMapper;
import com.home.expenses_control.model.Transaction;
import com.home.expenses_control.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SummaryServiceImpl implements SummaryService {

    private static final int MAX_PAGE_SIZE = 50;

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public SummaryServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SummaryResponseDto getTransactionsSummary(int page, int pageSize, LocalDate from, LocalDate to) {
        validatePagination(page, pageSize);
        validateDates(from, to);

        var totals = transactionRepository.getSummaryTotalsByDates(from, to);
        BigDecimal totalIncome = totals.totalIncome();
        BigDecimal totalExpenses = totals.totalExpenses();
        BigDecimal net = totalIncome.subtract(totalExpenses);
        var transactionsPaginated = getTransactionsPaginated(page, pageSize, from, to);
        var categoryBreakdown = transactionRepository.getCategoryBreakdownByDates(from, to);
        var dailyBreakdown = transactionRepository.getDailyBreakdownByDates(from, to);

        return new SummaryResponseDto(
            from,
            to,
            "GBP",
            totalIncome,
            totalExpenses,
            net,
            categoryBreakdown,
            dailyBreakdown,
            transactionsPaginated.transactions(),
            transactionsPaginated.pagination()
        );

    }

    private TransactionResponseDtoPaginated getTransactionsPaginated(int page, int pageSize, LocalDate from, LocalDate to){
        Specification<Transaction> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("status"), Status.ACTIVE),
            criteriaBuilder.between(root.get("date"), from, to)
        );
        Page<Transaction> transactionPage = transactionRepository.getAllTransactions(spec, page, pageSize);
        var transactions = transactionPage.getContent().stream().map(transactionMapper::fromModelToDto).toList();
        var pagination = new PaginationResponseDto(
            transactionPage.getNumber() + 1,
            transactionPage.getSize(),
            transactionPage.getTotalElements(),
            transactionPage.getTotalPages()
        );

        return new TransactionResponseDtoPaginated(transactions, pagination);
    }

    private void validatePagination(Integer page, Integer pageSize){
        if(page == null || page < 1){
            throw new InvalidPageException("Page number must exist and be greater than zero");
        }

        if(pageSize == null || pageSize < 1){
            throw new InvalidPageSizeException("Page size must exist and be greater than zero");
        }

        if(pageSize > MAX_PAGE_SIZE){
            throw new InvalidPageSizeException("Page size must not exceed " + MAX_PAGE_SIZE);
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate){
        if(startDate == null || endDate == null) {
            throw new InvalidDateException("Both start and end date must be informed together.");
        }

        if(startDate.isAfter(endDate)) {
            throw new InvalidDateException("Start date must be before or equal to end date.");
        }
    }
}
