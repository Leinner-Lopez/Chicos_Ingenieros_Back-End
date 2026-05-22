package com.chicos_ingenieros.zenkai.Products.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
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
        ProductEntity saved = repository.save(mapper.ProducttoProductEntity(product));
        return mapper.ProductEntitytoProduct(saved);
    }

    @Override
    public Product findById(Long id) {

        return repository.findById(id).map(mapper::ProductEntitytoProduct).orElseThrow(() ->
                new ResourceNotFoundException("Product wit id "+id+" not found"));
    }

    @Override
    public List<ProductDTO> findAll() {
        return repository.findCatalogProductsWithStock();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countProducts() {
        return repository.count();
    }

    @Override
    public Long countProductsWithCriticalStock() {
        return repository.countProductWithCriticalStock(LotStatus.AVAILABLE);
    }

    @Override
    public List<ProductInventoryDTO> findGeneralInventory() {
        return repository.findGeneralInventory(LotStatus.AVAILABLE);
    }

    @Override
    public List<ProductDTO> findByCategory(Long categoryId) {
        return repository.findByCategory_CategoryId(categoryId);
    }
}
