package com.home.expenses_control.dto.response;

import com.home.expenses_control.enums.Status;

import java.util.UUID;

public record CategoryResponseDto(
    UUID id,
    String name,
    String description,
    Status status
) {
}
