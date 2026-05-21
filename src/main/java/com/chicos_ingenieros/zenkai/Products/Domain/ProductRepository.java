package com.chicos_ingenieros.zenkai.Products.Domain;

import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);

    Product findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    Long countProducts();

    Long countProductsWithCriticalStock();

    List<ProductInventoryDTO> findGeneralInventory();
}
