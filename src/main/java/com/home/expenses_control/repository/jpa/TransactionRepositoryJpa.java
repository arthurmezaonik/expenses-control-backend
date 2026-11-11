package com.home.expenses_control.repository.jpa;

import com.home.expenses_control.dto.response.CategoryBreakdownResponseDto;
import com.home.expenses_control.dto.response.DailyBreakdownResponseDto;
import com.home.expenses_control.dto.response.SummaryTotalsDto;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.enums.Type;
import com.home.expenses_control.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryJpa extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    @Query("""
        SELECT new com.home.expenses_control.dto.response.SummaryTotalsDto(
            SUM(CASE WHEN t.type = :incomeType THEN t.amount ELSE 0 END),
            SUM(CASE WHEN t.type = :expenseType THEN t.amount ELSE 0 END)
        )
        FROM Transaction t
        WHERE t.status = :status
          AND t.date BETWEEN :from AND :to
        """)
    SummaryTotalsDto getSummaryTotalsByDates(
        @Param("incomeType") Type incomeType,
        @Param("expenseType") Type expenseType,
        @Param("status") Status status,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );

    @Query("""
        SELECT new com.home.expenses_control.dto.response.CategoryBreakdownResponseDto(
            c.name,
            COALESCE(SUM(CASE WHEN t.type = :incomeType THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.type = :expenseType THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.type = :incomeType THEN t.amount ELSE 0 END), 0)
                - COALESCE(SUM(CASE WHEN t.type = :expenseType THEN t.amount ELSE 0 END), 0)
        )
        FROM Transaction t
        JOIN t.category c
        WHERE t.status = :status
          AND t.date BETWEEN :from AND :to
        GROUP BY c.id, c.name
        ORDER BY c.name
        """)
    List<CategoryBreakdownResponseDto> getCategoryBreakdownByDates(
        @Param("incomeType") Type incomeType,
        @Param("expenseType") Type expenseType,
        @Param("status") Status status,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );

    @Query("""
        SELECT new com.home.expenses_control.dto.response.DailyBreakdownResponseDto(
            t.date,
            COALESCE(SUM(CASE WHEN t.type = :incomeType THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.type = :expenseType THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.type = :incomeType THEN t.amount ELSE 0 END), 0)
                - COALESCE(SUM(CASE WHEN t.type = :expenseType THEN t.amount ELSE 0 END), 0)
        )
        FROM Transaction t
        WHERE t.status = :status
          AND t.date BETWEEN :from AND :to
        GROUP BY t.date
        ORDER BY t.date
        """)
    List<DailyBreakdownResponseDto> getDailyBreakdownByDates(
        @Param("incomeType") Type incomeType,
        @Param("expenseType") Type expenseType,
        @Param("status") Status status,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );
}
