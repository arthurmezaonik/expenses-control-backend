package com.home.expenses_control.repository;

import com.home.expenses_control.dto.response.CategoryBreakdownResponseDto;
import com.home.expenses_control.dto.response.DailyBreakdownResponseDto;
import com.home.expenses_control.dto.response.SummaryTotalsDto;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.enums.Type;
import com.home.expenses_control.exceptions.TransactionNotFoundException;
import com.home.expenses_control.model.Transaction;
import com.home.expenses_control.repository.jpa.TransactionRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    TransactionRepositoryJpa repositoryJpa;

    public TransactionRepositoryImpl(TransactionRepositoryJpa repositoryJpa) {
        this.repositoryJpa = repositoryJpa;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return repositoryJpa.save(transaction);
    }

    @Override
    public Transaction getTransactionById(UUID transactionId) {
        var transaction = repositoryJpa.findById(transactionId);
        return getTransactionModel(transaction);
    }

    @Override
    public Page<Transaction> getAllTransactions(Specification<Transaction> spec, Integer page, Integer pageSize) {
        return repositoryJpa.findAll(spec, PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "date")));
    }

    @Override
    public SummaryTotalsDto getSummaryTotalsByDates(LocalDate from, LocalDate to) {
        return repositoryJpa.getSummaryTotalsByDates(Type.INCOME, Type.EXPENSE, Status.ACTIVE, from, to);
    }

    @Override
    public List<CategoryBreakdownResponseDto> getCategoryBreakdownByDates(LocalDate from, LocalDate to) {
        return repositoryJpa.getCategoryBreakdownByDates(Type.INCOME, Type.EXPENSE, Status.ACTIVE, from, to);
    }

    @Override
    public List<DailyBreakdownResponseDto> getDailyBreakdownByDates(LocalDate from, LocalDate to) {
        return repositoryJpa.getDailyBreakdownByDates(Type.INCOME, Type.EXPENSE, Status.ACTIVE, from, to);
    }

    private Transaction getTransactionModel(Optional<Transaction> transaction){
        return transaction.orElseThrow(TransactionNotFoundException::new);
    }
}
