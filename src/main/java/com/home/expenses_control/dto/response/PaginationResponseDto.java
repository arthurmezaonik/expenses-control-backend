package com.home.expenses_control.dto.response;

public record PaginationResponseDto(
    int currentPage,
    int pageSize,
    long totalElements,
    int totalPages
) { }
