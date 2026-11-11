package com.home.expenses_control.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
    @NotBlank(message = "Name can not be blank.")
    @Size(min = 3, max = 120, message = "Name must be between 3 and 120 characters")
    String name,
    @Nullable
    @Size(max = 255)
    String description
) {
    public CategoryRequestDto {
        if (name != null) {
            name = name.trim();
        }
    }
}
