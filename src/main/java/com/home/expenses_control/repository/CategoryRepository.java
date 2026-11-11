package com.home.expenses_control.repository;

import com.home.expenses_control.model.Category;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CategoryRepository {
    Category saveCategory(Category category);
    boolean checkIfNameIsRegistered(String name);
    Page<Category> getAllCategories(Integer page, Integer pageSize);
    Category getCategoryById(UUID id);
}
