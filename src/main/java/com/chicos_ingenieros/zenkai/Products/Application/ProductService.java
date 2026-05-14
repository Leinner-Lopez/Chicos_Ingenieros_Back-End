package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Products.Application.UseCases.ProductCountUseCase;
import com.chicos_ingenieros.zenkai.Products.Application.UseCases.ProductCrudUseCase;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductCrudUseCase, ProductCountUseCase {

    private final ProductRepository repository;
    private final ProductDTOMapper mapper;

    @Override
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public Product findProductById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findAllProducts() {
        return repository.findAll().stream()
                .map(mapper::productToProductDTO).toList();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product ProductDB = repository.findById(id);
        ProductDB.setName(product.getName());
        ProductDB.setPrice(product.getPrice());
        ProductDB.setDescription(product.getDescription());
        ProductDB.setMinStock(product.getMinStock());
        ProductDB.setCategoryId(product.getCategoryId());
        return repository.save(ProductDB);
    }

    @Override
    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countProducts() {
        return repository.countProducts();
    }
}
