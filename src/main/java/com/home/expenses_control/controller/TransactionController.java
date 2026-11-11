package com.home.expenses_control.controller;

import com.home.expenses_control.dto.response.TransactionResponseDto;
import com.home.expenses_control.dto.request.TransactionRequestDto;
import com.home.expenses_control.dto.response.TransactionResponseDtoPaginated;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    private ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto transactionData) {
        log.info("Creating new transaction, data: {}", transactionData);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.registerNewTransaction(transactionData));
    }

    @GetMapping
    private ResponseEntity<TransactionResponseDtoPaginated> getTransactionsList(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false, defaultValue = "ACTIVE") Status status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
        log.info("Get transactions list: getTransactionsList(), page = {}, pageSize = {}, status = {}, startDate = {}, endDate = {}", page, pageSize, status, from, to);
        return ResponseEntity.ok().body(transactionService.getTransactionsList(page, pageSize, status, from, to));
    }

    @GetMapping("/{id}")
    private ResponseEntity<TransactionResponseDto> readTransactionById(@PathVariable UUID id){
        log.info("Reading transaction by id {}", id);
        return ResponseEntity.ok().body(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable UUID id, @Valid @RequestBody TransactionRequestDto newTransactionData) {
        log.info("Updating transaction by id {}, new transaction data {}", id, newTransactionData);
        return ResponseEntity.ok().body(transactionService.updateTransaction(id, newTransactionData));
    }

    @PatchMapping("/{id}/status/deactivate")
    private ResponseEntity<TransactionResponseDto> deactivateTransaction(@PathVariable UUID id){
        log.info("Deactivate transaction status by id {}", id);
        return ResponseEntity.ok().body(transactionService.deactivateTransaction(id));
    }

    @PatchMapping("/{id}/status/activate")
    private ResponseEntity<TransactionResponseDto> activateTransaction(@PathVariable UUID id){
        log.info("Activate transaction status by id {}", id);
        return ResponseEntity.ok().body(transactionService.activateTransaction(id));
    }
}
