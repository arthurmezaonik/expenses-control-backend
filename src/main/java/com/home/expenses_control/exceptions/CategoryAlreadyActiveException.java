package com.home.expenses_control.exceptions;

public class CategoryAlreadyActiveException extends RuntimeException {
    public CategoryAlreadyActiveException(String message) {
        super(message);
    }

    public CategoryAlreadyActiveException() {
        this("The category is already active.");
    }
}
