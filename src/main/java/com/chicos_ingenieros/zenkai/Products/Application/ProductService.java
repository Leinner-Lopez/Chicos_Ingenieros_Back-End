package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Categories.Application.CategoryService;
import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
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
    private final CategoryService categoryService;

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

    public Product updateProduct(Long id, Product product) {
        Product ProductDB = repository.findById(id);
        ProductDB.setName(product.getName());
        ProductDB.setPrice(product.getPrice());
        ProductDB.setDescription(product.getDescription());
        ProductDB.setMin_stock(product.getMin_stock());
        ProductDB.setCategory(product.getCategory());
        return repository.save(ProductDB);
    }

    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }
}
