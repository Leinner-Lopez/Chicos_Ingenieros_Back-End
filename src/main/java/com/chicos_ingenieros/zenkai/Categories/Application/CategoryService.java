package com.chicos_ingenieros.zenkai.Categories.Application;

import com.chicos_ingenieros.zenkai.Categories.Application.UseCases.CategoryCountUseCase;
import com.chicos_ingenieros.zenkai.Categories.Application.UseCases.CategoryCrudUseCase;
import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Categories.Domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService implements CategoryCrudUseCase, CategoryCountUseCase {

    private final CategoryRepository repository;

    @Override
    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    @Override
    public Category findCategoryById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Category> findAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category categoryDB = repository.findById(id);
        categoryDB.setName(category.getName());
        categoryDB.setDescription(category.getDescription());
        return repository.save(categoryDB);
    }

    @Override
    public void deleteCategoryById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countCategories() {
        return repository.countCategories();
    }
}
