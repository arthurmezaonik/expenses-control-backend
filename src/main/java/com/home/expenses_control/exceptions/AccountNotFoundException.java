package com.home.expenses_control.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException() {
        this("Account not found");
    }
}
