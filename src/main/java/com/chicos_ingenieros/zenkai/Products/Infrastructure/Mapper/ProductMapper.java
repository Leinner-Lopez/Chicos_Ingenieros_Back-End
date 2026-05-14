package com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper;


import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper.CategoryMapper;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}, imports = {CategoryEntity.class})
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.name")
    Product ProductEntitytoProduct(ProductEntity entity);

    @Mapping(target = "category",
            expression = "java(CategoryEntity.builder().categoryId(product.getCategoryId()).build())")
    ProductEntity ProducttoProductEntity(Product product);
}
