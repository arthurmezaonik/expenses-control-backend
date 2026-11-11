package com.home.expenses_control.repository;

import com.home.expenses_control.exceptions.CategoryNotFoundException;
import com.home.expenses_control.exceptions.InvalidPageException;
import com.home.expenses_control.model.Category;
import com.home.expenses_control.repository.jpa.CategoryRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryRepositoryJpa categoryRepositoryJpa;

    public CategoryRepositoryImpl(CategoryRepositoryJpa categoryRepositoryJpa) {
        this.categoryRepositoryJpa = categoryRepositoryJpa;
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepositoryJpa.save(category);
    }

    @Override
    public boolean checkIfNameIsRegistered(String name) {
        return categoryRepositoryJpa.existsByNameIgnoreCase(name);
    }

    @Override
    public Page<Category> getAllCategories(Integer page, Integer pageSize) {
        return categoryRepositoryJpa.findAll(PageRequest.of(page - 1, pageSize));
    }

    @Override
    public Category getCategoryById(UUID id) {
        return getCategoryModel(categoryRepositoryJpa.findById(id));
    }

    private Category getCategoryModel(Optional<Category> category) {
        return category.orElseThrow(CategoryNotFoundException::new);
    }
}
