package com.chicos_ingenieros.zenkai.Products.Application.UseCases;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;

import java.util.List;

public interface ProductCrudUseCase {
    Product saveProduct(Product product);
    Product findProductById(Long id);
    List<ProductDTO> findAllProducts();
    Product updateProduct(Long id, Product product);
    void deleteProductById(Long id);
}
