package com.chicos_ingenieros.zenkai.Products.Application.UseCases;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductCrudUseCase {
    Product saveProduct(Product product, MultipartFile image) throws IOException;
    Product findProductById(Long id);
    List<ProductDTO> findAllProducts();
    Product updateProduct(Long id, Product product);
    void deleteProductById(Long id);
}
