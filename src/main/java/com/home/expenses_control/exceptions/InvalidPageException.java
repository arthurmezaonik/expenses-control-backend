package com.home.expenses_control.exceptions;

public class InvalidPageException extends RuntimeException {
    public InvalidPageException(String message) {
        super(message);
    }

    public InvalidPageException() {
        this("Page is invalid");
    }
}
