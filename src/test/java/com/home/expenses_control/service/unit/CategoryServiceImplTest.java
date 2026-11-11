package com.home.expenses_control.service.unit;

import com.home.expenses_control.dto.request.CategoryRequestDto;
import com.home.expenses_control.dto.response.CategoryResponseDto;
import com.home.expenses_control.enums.Status;
import com.home.expenses_control.exceptions.CategoryNameAlredyRegisteredException;
import com.home.expenses_control.exceptions.InvalidPageSizeException;
import com.home.expenses_control.mapper.CategoryMapper;
import com.home.expenses_control.model.Category;
import com.home.expenses_control.repository.CategoryRepository;
import com.home.expenses_control.service.CategoryService;
import com.home.expenses_control.service.CategoryServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    private static CategoryRequestDto validRequest() {
        return new CategoryRequestDto("Household", "Household expenses");
    }

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
    }

    @Nested
    class RegisterNewCategory{
        @Test
        @DisplayName("Should register a new category with success")
        void shouldRegisterNewCategoryWithSuccess(){
            // Arrange
            CategoryRequestDto categoryData = validRequest();
            Category categoryModel = new Category(categoryData.name(), categoryData.description());
            Category savedCategory = new Category(categoryData.name(), categoryData.description());
            var expectedResponse = new CategoryResponseDto(
                UUID.randomUUID(),
                "Household",
                "Household expenses",
                Status.ACTIVE
            );

            // false = name is not registered
            when(categoryRepository.checkIfNameIsRegistered(categoryData.name())).thenReturn(false);
            when(categoryMapper.fromDtoToModel(categoryData)).thenReturn(categoryModel);
            when(categoryRepository.saveCategory(categoryModel)).thenReturn(savedCategory);
            when(categoryMapper.fromModelToDto(savedCategory)).thenReturn(expectedResponse);

            ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

            // Act
            var actualResponse = categoryService.registerNewCategory(categoryData);

            // Assert
            verify(categoryRepository).checkIfNameIsRegistered(categoryData.name());
            verify(categoryMapper).fromDtoToModel(categoryData);
            verify(categoryRepository).saveCategory(captor.capture());

            var actual = captor.getValue();

            assertThat(actual).isNotNull();
            assertThat(actual.getName()).isEqualTo(categoryData.name());
            assertThat(actual.getDescription()).isEqualTo(categoryData.description());
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Should throw an exception if trying to register with a name that is already in use")
        void shouldThrowAnExceptionForDuplicateName(){
            // Arrange
            CategoryRequestDto categoryData = validRequest();

            // true = name is already registered
            when(categoryRepository.checkIfNameIsRegistered("Household")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.registerNewCategory(categoryData))
                .isInstanceOf(CategoryNameAlredyRegisteredException.class)
                .hasMessage("Another category is already registered with this name");
            verify(categoryMapper, never()).fromDtoToModel(categoryData);
            verify(categoryRepository, never()).saveCategory(any(Category.class));
        }
    }

    @Nested
    class GetCategoryModelById {
        @Test
        @DisplayName("Should return a category for a valid ID")
        void shouldReturnCategoryModelById(){
            // Arrange
            UUID id = UUID.randomUUID();
            CategoryRequestDto categoryData = validRequest();
            Category categoryModel = new Category(categoryData.name(), categoryData.description());

            when(categoryRepository.getCategoryById(id)).thenReturn(categoryModel);

            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            var actualResponse = categoryService.getCategoryModelById(id);

            // Assert
            verify(categoryRepository).getCategoryById(captor.capture());

            var actual = captor.getValue();
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(id);
            assertThat(actualResponse.getName()).isEqualTo(categoryData.name());
            assertThat(actualResponse.getDescription()).isEqualTo(categoryData.description());
        }
    }

    @Nested
    class GetCategoriesList {

        @Test
        @DisplayName("Should allow the maximum page size")
        void shouldAllowMaximumPageSize() {
            var pageRequest = PageRequest.of(0, 100);
            when(categoryRepository.getAllCategories(1, 100))
                .thenReturn(new PageImpl<>(List.of(), pageRequest, 0));

            var actual = categoryService.getCategoriesList(1, 100);

            assertThat(actual.pagination().pageSize()).isEqualTo(100);
            verify(categoryRepository).getAllCategories(1, 100);
        }

        @Test
        @DisplayName("Should reject a page size greater than the maximum")
        void shouldRejectPageSizeGreaterThanMaximum() {
            assertThatThrownBy(() -> categoryService.getCategoriesList(1, 101))
                .isInstanceOf(InvalidPageSizeException.class)
                .hasMessage("Page size must not exceed 100");

            verifyNoInteractions(categoryRepository);
        }
    }


}
