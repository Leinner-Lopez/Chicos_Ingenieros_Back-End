package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Spy
    private ProductDTOMapper mapper = Mappers.getMapper(ProductDTOMapper.class);

    @InjectMocks
    private ProductService service;

    Category lacteos = Category.builder()
            .category_id(1L)
            .name("Lácteos")
            .description("Productos derivados de la leche").build();

    Product product = Product.builder()
            .product_id(null)
            .name("Leche")
            .price(new BigDecimal("3500.00"))
            .description("Bolsa 1L")
            .min_stock(10)
            .category(lacteos).build();

    Product savedProduct = Product.builder()
            .product_id(1L)
            .name("Leche")
            .price(new BigDecimal("3500.00"))
            .description("Bolsa 1L")
            .min_stock(10)
            .category(lacteos).build();

    Product editProduct = Product.builder()
            .product_id(1L)
            .name("Queso")
            .price(new BigDecimal("4500.00"))
            .description("Tipo Campesino")
            .min_stock(10)
            .category(lacteos).build();

    @Test
    void saveProduct() {
        when(repository.save(product)).thenReturn(savedProduct);
        Product result = service.saveProduct(product);
        assertEquals(savedProduct, result);
    }

    @Test
    void findProductById() {
        when(repository.findById(1L)).thenReturn(savedProduct);
        Product result = service.findProductById(1L);
        assertEquals(savedProduct, result);
    }

    @Test
    void findAllProducts() {
        when(repository.findAll()).thenReturn(List.of(savedProduct));
        List<ProductDTO> result = service.findAllProducts();
        assertEquals("Leche", result.getFirst().name());
    }

    @Test
    void updateProduct() {
        when(repository.findById(1L)).thenReturn(savedProduct);
        when(repository.save(savedProduct)).thenReturn(editProduct);
        Product result = service.updateProduct(1L, savedProduct);
        assertEquals(editProduct, result);
    }

    @Test
    void deleteProductById() {
        service.deleteProductById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}