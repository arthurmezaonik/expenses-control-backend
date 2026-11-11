package com.home.expenses_control.dto.response;

import java.util.List;

public record TransactionResponseDtoPaginated(
    List<TransactionResponseDto> transactions,
    PaginationResponseDto pagination
) {
}
