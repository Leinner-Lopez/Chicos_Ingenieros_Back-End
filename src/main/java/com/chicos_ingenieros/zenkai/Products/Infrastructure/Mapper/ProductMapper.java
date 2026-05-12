package com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper;


import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper.CategoryMapper;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class })
public interface ProductMapper {
    @Mapping(target = "lots", ignore = true)
    Product toDomain(ProductEntity entity);

    @Mapping(target = "lots", ignore = true)
    ProductEntity toEntity(Product product);
}
