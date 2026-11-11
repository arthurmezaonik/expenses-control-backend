package com.home.expenses_control.dto.response;

import java.time.Instant;

public record LoginResponseDto(
    String accessToken,
    String tokenType,
    Instant expiresAt
) {
}
