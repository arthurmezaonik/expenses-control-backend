package com.home.expenses_control.dto.response;

import java.util.UUID;

public record CurrentAccountResponseDto(
    UUID id,
    String firstName,
    String lastName,
    String email
) {
}
