package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Cloudinary.Application.CloudinaryService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ProductRepository repository;

    @Spy
    private ProductDTOMapper mapper = Mappers.getMapper(ProductDTOMapper.class);

    @InjectMocks
    private ProductService service;

    Category lacteos = Category.builder()
            .categoryId(1L)
            .name("Lácteos")
            .description("Productos derivados de la leche").build();

    Product product = Product.builder()
            .productId(null)
            .name("Leche")
            .price(new BigDecimal("3500.00"))
            .description("Bolsa 1L")
            .minStock(10)
            .categoryId(lacteos.getCategoryId()).build();

    Product savedProduct = Product.builder()
            .productId(1L)
            .name("Leche")
            .price(new BigDecimal("3500.00"))
            .description("Bolsa 1L")
            .minStock(10)
            .categoryId(lacteos.getCategoryId()).build();

    Product editProduct = Product.builder()
            .productId(1L)
            .name("Queso")
            .price(new BigDecimal("4500.00"))
            .description("Tipo Campesino")
            .minStock(10)
            .categoryId(lacteos.getCategoryId()).build();


    @Test
    void saveProduct() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        String fakeUrl = "https://res.cloudinary.com/fake/image.jpg";
        when(cloudinaryService.uploadImage(image)).thenReturn(fakeUrl);
        when(repository.save(any(Product.class))).thenReturn(savedProduct);
        Product result = service.saveProduct(product, image);
        assertEquals(savedProduct, result);
        verify(cloudinaryService, times(1)).uploadImage(image);
        verify(repository, times(1)).save(product);
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

    @Test
    void countProducts(){
        when(repository.countProducts()).thenReturn(5L);
        Long result = service.countProducts();
        assertEquals(5L, result);
    }
}