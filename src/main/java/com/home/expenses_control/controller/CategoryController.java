package com.home.expenses_control.controller;

import com.home.expenses_control.dto.response.CategoryResponseDto;
import com.home.expenses_control.dto.response.CategoryResponseDtoPaginated;
import com.home.expenses_control.dto.request.CategoryRequestDto;
import com.home.expenses_control.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryResponseDtoPaginated> getCategoriesList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("Get categories list: getCategoriesList(), page = {}, pageSize = {}", page, pageSize);
        return ResponseEntity.ok(categoryService.getCategoriesList(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable UUID id) {
        log.info("Get category by it's id: getCategoryById(), id = {}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto newCategoryData) {
        log.info("Create new category: createCategory(), data = {}", newCategoryData);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.registerNewCategory(newCategoryData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> editCategory(@PathVariable UUID id, @Valid @RequestBody CategoryRequestDto newCategoryData) {
        log.info("Edit category: editCategory(), id = {}, newCategoryData = {}", id, newCategoryData);
        return ResponseEntity.ok(categoryService.editCategoryById(id, newCategoryData));
    }

    @PatchMapping("/{id}/status/deactivate")
    public ResponseEntity<CategoryResponseDto> deactivateCategory(@PathVariable UUID id) {
        log.info("Deactivate category: deactivateCategory(), id = {}", id);
        return ResponseEntity.ok(categoryService.deactivateCategory(id));
    }

    @PatchMapping("/{id}/status/activate")
    public ResponseEntity<CategoryResponseDto> activateCategory(@PathVariable UUID id) {
        log.info("Activate category: activateCategory(), id = {}", id);
        return ResponseEntity.ok(categoryService.activateCategory(id));
    }
}
