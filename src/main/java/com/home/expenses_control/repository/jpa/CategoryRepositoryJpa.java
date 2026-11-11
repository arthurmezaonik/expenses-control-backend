package com.home.expenses_control.repository.jpa;

import com.home.expenses_control.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepositoryJpa extends JpaRepository<Category, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
