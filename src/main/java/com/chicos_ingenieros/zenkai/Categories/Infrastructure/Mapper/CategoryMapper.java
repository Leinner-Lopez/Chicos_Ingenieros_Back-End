package com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "products", ignore = true)
    Category categoryEntityToCategory(CategoryEntity categoryEntity);

    @Mapping(target = "products", ignore = true)
    CategoryEntity categoryToCategoryEntity(Category category);
}
