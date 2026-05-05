package com.chicos_ingenieros.zenkai.Categories.Application;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Categories.Domain.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    Category category = Category.builder()
            .category_id(null)
            .name("Lácteos")
            .description("Productos derivados de la leche").build();

    Category savedCategory = Category.builder()
            .category_id(1L)
            .name("Lácteos")
            .description("Productos derivados de la leche").build();

    Category editCategory = Category.builder()
            .category_id(1L)
            .name("Carnes")
            .description("Productos derivados de la carne animal").build();

    @Test
    void saveCategory() {
        when(repository.save(category)).thenReturn(savedCategory);
        Category result = service.saveCategory(category);
        assertEquals(savedCategory, result);
    }

    @Test
    void findCategoryById() {
        when(repository.findById(1L)).thenReturn(savedCategory);
        Category result = service.findCategoryById(1L);
        assertEquals(savedCategory, result);
    }

    @Test
    void findAllCategories() {
        when(repository.findAll()).thenReturn(List.of(savedCategory));
        List<Category> result = service.findAllCategories();
        assertEquals(List.of(savedCategory), result);
    }

    @Test
    void updateCategory() {
        when(repository.findById(1L)).thenReturn(savedCategory);
        when(repository.save(savedCategory)).thenReturn(editCategory);
        Category result = service.updateCategory(1L, editCategory);
        assertEquals(editCategory.getName(), result.getName());
    }

    @Test
    void deleteCategoryById() {
        service.deleteCategoryById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}