package com.chicos_ingenieros.zenkai.Categories.Application.UseCases;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;

import java.util.List;

public interface CategoryCrudUseCase {
    Category saveCategory(Category category);
    Category findCategoryById(Long id);
    List<Category> findAllCategories();
    Category updateCategory(Long id, Category category);
    void deleteCategoryById(Long id);
}
