package com.chicos_ingenieros.zenkai.Categories.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Categories.Domain.CategoryRepository;
import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper.CategoryMapper;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryMySqlRepository implements CategoryRepository {

    private final SpringCategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryEntity saved = repository.save(mapper.categoryToCategoryEntity(category));
        return mapper.categoryEntityToCategory(saved);
    }

    @Override
    public Category findById(Long id) {
        return repository.findById(id).map(mapper::categoryEntityToCategory).orElseThrow(() ->
                new ResourceNotFoundException("Category with id " + id + " not found")
        );
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll().stream().map(mapper::categoryEntityToCategory).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
