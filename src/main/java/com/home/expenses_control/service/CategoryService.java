package com.home.expenses_control.service;

import com.home.expenses_control.dto.response.CategoryResponseDto;
import com.home.expenses_control.dto.response.CategoryResponseDtoPaginated;
import com.home.expenses_control.dto.request.CategoryRequestDto;
import com.home.expenses_control.model.Category;

import java.util.UUID;

public interface CategoryService {
    Category getCategoryModelById(UUID id);
    CategoryResponseDto registerNewCategory(CategoryRequestDto newCategoryData);
    CategoryResponseDtoPaginated getCategoriesList(Integer page, Integer pageSize);
    CategoryResponseDto getCategoryById(UUID categoryId);
    CategoryResponseDto editCategoryById(UUID categoryId, CategoryRequestDto newCategoryData);
    CategoryResponseDto deactivateCategory(UUID categoryId);
    CategoryResponseDto activateCategory(UUID categoryId);
}
