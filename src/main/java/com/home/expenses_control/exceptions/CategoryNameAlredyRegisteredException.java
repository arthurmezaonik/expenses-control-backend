package com.home.expenses_control.exceptions;

public class CategoryNameAlredyRegisteredException extends RuntimeException {
    public CategoryNameAlredyRegisteredException(String message) {
        super(message);
    }

    public CategoryNameAlredyRegisteredException() {
        this("Another category is already registered with this name");
    }
}
