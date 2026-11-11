package com.home.expenses_control.exceptions;

public class TransactionAlreadyInactiveException extends RuntimeException {
    public TransactionAlreadyInactiveException(String message) {
        super(message);
    }

    public TransactionAlreadyInactiveException() {
        this("This transaction is already inactive.");
    }
}
