package com.home.expenses_control.mapper;

import com.home.expenses_control.dto.response.CategoryResponseDto;
import com.home.expenses_control.dto.request.CategoryRequestDto;
import com.home.expenses_control.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto fromModelToDto(Category category) {
        return new CategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getStatus()
        );
    }

    public Category fromDtoToModel(CategoryRequestDto newCategoryData) {
        return new Category(
            newCategoryData.name(),
            newCategoryData.description()
        );
    }
}
