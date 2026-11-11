package com.home.expenses_control.exceptions;

public class InactiveCategoryException extends RuntimeException {
    public InactiveCategoryException(String message) {
        super(message);
    }

  public InactiveCategoryException() {
      this(
          "Inactive category."
      );
  }
}
