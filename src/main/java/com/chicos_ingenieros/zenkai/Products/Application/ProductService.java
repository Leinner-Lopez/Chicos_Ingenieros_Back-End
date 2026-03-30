package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public Product findProductById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }
}
