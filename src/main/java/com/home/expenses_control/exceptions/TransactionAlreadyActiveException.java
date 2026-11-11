package com.home.expenses_control.exceptions;

public class TransactionAlreadyActiveException extends RuntimeException {
    public TransactionAlreadyActiveException(String message) {
        super(message);
    }

    public TransactionAlreadyActiveException() {
        this("This transaction is already active.");
    }
}
