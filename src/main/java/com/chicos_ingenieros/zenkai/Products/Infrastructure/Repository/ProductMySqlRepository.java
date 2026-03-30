package com.chicos_ingenieros.zenkai.Products.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductMySqlRepository implements ProductRepository {

    private final SpringProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity saved = repository.save(mapper.toEntity(product));
        return mapper.toDomain(saved);
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
