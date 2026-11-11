package com.home.expenses_control.service;

import com.home.expenses_control.dto.response.TransactionResponseDto;
import com.home.expenses_control.dto.request.TransactionRequestDto;
import com.home.expenses_control.dto.response.TransactionResponseDtoPaginated;
import com.home.expenses_control.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public interface TransactionService {
    TransactionResponseDto registerNewTransaction(TransactionRequestDto transactionData);
    TransactionResponseDto getTransactionById(UUID transactionId);
    TransactionResponseDto updateTransaction(UUID transactionId, TransactionRequestDto newTransactionData);
    TransactionResponseDto deactivateTransaction(UUID transactionId);
    TransactionResponseDto activateTransaction(UUID transactionId);
    TransactionResponseDtoPaginated getTransactionsList(Integer page, Integer pageSize, Status status, LocalDate from, LocalDate to);
}
