package com.home.expenses_control.exceptions;

public class CategoryAlreadyInactiveException extends RuntimeException {
    public CategoryAlreadyInactiveException(String message) {
        super(message);
    }

    public CategoryAlreadyInactiveException() {
        this("The category is already inactive.");
    }
}
