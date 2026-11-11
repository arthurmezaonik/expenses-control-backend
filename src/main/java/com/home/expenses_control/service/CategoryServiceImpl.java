package com.home.expenses_control.service;

import com.home.expenses_control.dto.response.CategoryResponseDto;
import com.home.expenses_control.dto.response.CategoryResponseDtoPaginated;
import com.home.expenses_control.dto.request.CategoryRequestDto;
import com.home.expenses_control.dto.response.PaginationResponseDto;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.exceptions.*;
import com.home.expenses_control.mapper.CategoryMapper;
import com.home.expenses_control.model.Category;
import com.home.expenses_control.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final int MAX_PAGE_SIZE = 100;

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category getCategoryModelById(UUID id) {
        return categoryRepository.getCategoryById(id);
    }

    @Override
    public CategoryResponseDto registerNewCategory(CategoryRequestDto newCategoryData) {
        // validate if name is already registered (case-insensitive)
        checkIfNameIsRegistered(newCategoryData.name());

        Category newCategory = categoryMapper.fromDtoToModel(newCategoryData);
        return categoryMapper.fromModelToDto(categoryRepository.saveCategory(newCategory));
    }

    @Override
    public CategoryResponseDtoPaginated getCategoriesList(Integer page, Integer pageSize) {
        validatePagination(page, pageSize);

        Page<Category> categoryPage = categoryRepository.getAllCategories(page, pageSize);

        var categories = categoryPage.getContent().stream().map(categoryMapper::fromModelToDto).toList();
        var pagination = new PaginationResponseDto(
            categoryPage.getNumber() + 1,
            categoryPage.getSize(),
            categoryPage.getTotalElements(),
            categoryPage.getTotalPages()
        );
        return new CategoryResponseDtoPaginated(categories, pagination);
    }

    @Override
    public CategoryResponseDto getCategoryById(UUID categoryId) {
        return categoryMapper.fromModelToDto(getCategoryModelById(categoryId));
    }

    @Override
    public CategoryResponseDto editCategoryById(UUID categoryId, CategoryRequestDto newCategoryData) {
        // get category
        Category category = getCategoryModelById(categoryId);

        // check if new name is different from old name
        if(!category.getName().equalsIgnoreCase(newCategoryData.name())) {
            // check if new name is already registered
            checkIfNameIsRegistered(newCategoryData.name());
        }

        category.update(newCategoryData.name(), newCategoryData.description());

        return categoryMapper.fromModelToDto(categoryRepository.saveCategory(category));
    }

    @Override
    public CategoryResponseDto deactivateCategory(UUID categoryId) {
        // get category
        Category category = getCategoryModelById(categoryId);

        // check current status
        if(category.getStatus().equals(Status.INACTIVE)){
            throw new CategoryAlreadyInactiveException();
        }

        category.deactivate();
        return categoryMapper.fromModelToDto(categoryRepository.saveCategory(category));
    }

    @Override
    public CategoryResponseDto activateCategory(UUID categoryId) {
        // get category
        Category category = getCategoryModelById(categoryId);

        // check current status
        if(category.getStatus().equals(Status.ACTIVE)){
            throw new CategoryAlreadyActiveException();
        }

        category.activate();
        return categoryMapper.fromModelToDto(categoryRepository.saveCategory(category));
    }

    private void checkIfNameIsRegistered(String name){
        if(categoryRepository.checkIfNameIsRegistered(name)){
            throw new CategoryNameAlredyRegisteredException();
        }
    }

    private void validatePagination(Integer page, Integer pageSize){
        if(page == null || page < 1){
            throw new InvalidPageException("Page number must exist and be greater than zero");
        }

        if(pageSize == null || pageSize < 1){
            throw new InvalidPageSizeException("Page size must exist and be greater than zero");
        }

        if(pageSize > MAX_PAGE_SIZE){
            throw new InvalidPageSizeException("Page size must not exceed " + MAX_PAGE_SIZE);
        }
    }
}
