package com.home.expenses_control.mapper;

import com.home.expenses_control.dto.request.TransactionRequestDto;
import com.home.expenses_control.dto.response.TransactionResponseDto;
import com.home.expenses_control.model.Category;
import com.home.expenses_control.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final CategoryMapper categoryMapper;

    public TransactionMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public Transaction fromDtoToModel(TransactionRequestDto transactionData, Category transactionCategory) {
        return new Transaction(
            transactionData.type(),
            transactionData.amount(),
            transactionData.date(),
            transactionCategory,
            transactionData.note()
        );
    }

    public TransactionResponseDto fromModelToDto(Transaction transaction) {
        return new TransactionResponseDto(
            transaction.getId(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getDate(),
            categoryMapper.fromModelToDto(transaction.getCategory()),
            transaction.getNote(),
            transaction.getStatus()
        );
    }
}
