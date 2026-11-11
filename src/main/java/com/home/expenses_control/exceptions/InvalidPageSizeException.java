package com.home.expenses_control.exceptions;

public class InvalidPageSizeException extends RuntimeException {
    public InvalidPageSizeException(String message) {
        super(message);
    }

    public InvalidPageSizeException() {
        this("Page size is invalid");
    }
}
