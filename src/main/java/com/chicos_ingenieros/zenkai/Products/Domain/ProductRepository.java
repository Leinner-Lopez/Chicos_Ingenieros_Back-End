package com.chicos_ingenieros.zenkai.Products.Domain;

import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);

    Product findById(Long id);

    List<ProductDTO> findAll();

    void deleteById(Long id);

    Long countProducts();

    Long countProductsWithCriticalStock();

    List<ProductInventoryDTO> findGeneralInventory();

    List<ProductDTO> findByCategory(Long categoryId);

    ProductDTO findProductDTOById(Long id);
}
