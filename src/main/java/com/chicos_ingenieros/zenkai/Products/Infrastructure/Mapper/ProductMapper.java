package com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper;


import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper.CategoryMapper;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class })
public interface ProductMapper {
    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product product);
}
