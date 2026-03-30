package com.chicos_ingenieros.zenkai.Products.Domain;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);

    Product findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);
}
