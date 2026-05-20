package com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDTOMapper {

    ProductDTO productToProductDTO(Product product);

    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "minStock", ignore = true)
    @Mapping(target = "description", ignore = true)
    Product productDTOToProduct(ProductDTO productDTO);
}
