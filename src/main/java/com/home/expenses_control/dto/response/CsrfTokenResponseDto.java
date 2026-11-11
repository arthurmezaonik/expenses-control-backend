package com.home.expenses_control.dto.response;

public record CsrfTokenResponseDto(
    String headerName,
    String token
) {
}
