package com.home.expenses_control.dto;

import com.home.expenses_control.dto.request.CategoryRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRequestDtoTest {

    @Test
    @DisplayName("Should trim the category name when created")
    void shouldTrimCategoryNameWhenCreated() {
        var request = new CategoryRequestDto("  Groceries  ", "Food shopping");

        assertThat(request.name()).isEqualTo("Groceries");
    }
}
