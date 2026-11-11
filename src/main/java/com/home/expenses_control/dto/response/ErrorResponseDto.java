package com.home.expenses_control.dto.response;

public record ErrorResponseDto(
    int status,
    String error,
    String message
) {
}
