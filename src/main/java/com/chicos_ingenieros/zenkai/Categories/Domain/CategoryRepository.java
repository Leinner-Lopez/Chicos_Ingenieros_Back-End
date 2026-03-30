package com.chicos_ingenieros.zenkai.Categories.Domain;

import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;

import java.util.List;

public interface CategoryRepository {

    Category save(Category product);

    Category findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);
}
