package com.home.expenses_control.dto.response;

import java.util.List;

public record CategoryResponseDtoPaginated(
    List<CategoryResponseDto> categories,
    PaginationResponseDto pagination
) {
}
