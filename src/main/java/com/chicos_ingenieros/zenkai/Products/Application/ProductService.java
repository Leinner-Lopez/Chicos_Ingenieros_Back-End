package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Cloudinary.Application.CloudinaryService;
import com.chicos_ingenieros.zenkai.Products.Application.UseCases.DashboardAdminUseCase;
import com.chicos_ingenieros.zenkai.Products.Application.UseCases.InventoryUseCase;
import com.chicos_ingenieros.zenkai.Products.Application.UseCases.ProductCountUseCase;
import com.chicos_ingenieros.zenkai.Products.Application.UseCases.ProductCrudUseCase;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductCrudUseCase, ProductCountUseCase, DashboardAdminUseCase, InventoryUseCase {

    private final ProductRepository repository;
    private final ProductDTOMapper mapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public Product saveProduct(Product product, MultipartFile image) throws IOException {
        String imageUrl = cloudinaryService.uploadImage(image);
        product.setImageUrl(imageUrl);
        return repository.save(product);
    }

    @Override
    public Product findProductById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product product, MultipartFile image) throws IOException {
        Product productDb = repository.findById(id);
        productDb.setName(product.getName());
        productDb.setPrice(product.getPrice());
        productDb.setDescription(product.getDescription());
        productDb.setMinStock(product.getMinStock());
        productDb.setCategoryId(product.getCategoryId());
        if (image != null && !image.isEmpty()) {
            productDb.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return repository.save(productDb);
    }

    @Override
    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countProducts() {
        return repository.countProducts();
    }

    @Override
    public Long productsWithCriticalStock() {
        return repository.countProductsWithCriticalStock();
    }

    @Override
    public List<ProductInventoryDTO> findGeneralInventory() {
        return repository.findGeneralInventory();
    }

    @Override
    public List<ProductDTO> findByCategory(Long categoryId) {
        return repository.findByCategory(categoryId);
    }

    @Override
    public ProductDTO findProductDTOById(Long id) {
        return repository.findProductDTOById(id);
    }
}
