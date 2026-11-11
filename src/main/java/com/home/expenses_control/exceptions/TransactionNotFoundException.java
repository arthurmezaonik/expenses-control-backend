package com.home.expenses_control.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException() {
        this("Transaction not found");
    }
}
