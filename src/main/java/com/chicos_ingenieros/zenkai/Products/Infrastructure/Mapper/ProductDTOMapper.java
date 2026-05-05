package com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper.CategoryMapper;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class })
public interface ProductDTOMapper {
    @Mapping(source= "category.name", target = "categoryName")
    ProductDTO productToProductDTO(Product product);
    @Mapping(source= "categoryName", target = "category.name")
    Product productDTOToProduct(ProductDTO productDTO);
}
