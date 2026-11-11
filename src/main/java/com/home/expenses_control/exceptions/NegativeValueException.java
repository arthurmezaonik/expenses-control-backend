package com.home.expenses_control.exceptions;

public class NegativeValueException extends RuntimeException {
    public NegativeValueException(String message) {
        super(message);
    }

    public NegativeValueException() {
        this("Amount value can't be a negative number.");
    }
}
