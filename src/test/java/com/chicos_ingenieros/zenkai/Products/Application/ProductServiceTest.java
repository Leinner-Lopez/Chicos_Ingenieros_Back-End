package com.chicos_ingenieros.zenkai.Products.Application;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Cloudinary.Application.CloudinaryService;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Domain.ProductRepository;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @InjectMocks
    private ProductService service;

    private Product productSetup;
    private Product savedProduct;
    private Product editProduct;
    private ProductDTO productDTO;
    private ProductInventoryDTO productInventoryDTO;

    @BeforeEach
    void setUp() {
        Category lacteos = Category.builder()
                .categoryId(1L)
                .name("Lácteos")
                .description("Productos derivados de la leche")
                .build();

        productSetup = Product.builder()
                .productId(null)
                .name("Leche")
                .price(new BigDecimal("3500.00"))
                .description("Bolsa 1L")
                .minStock(10)
                .categoryId(lacteos.getCategoryId())
                .build();

        savedProduct = Product.builder()
                .productId(1L)
                .name("Leche")
                .price(new BigDecimal("3500.00"))
                .description("Bolsa 1L")
                .minStock(10)
                .imageUrl("https://res.cloudinary.com/fake/image.jpg")
                .categoryId(lacteos.getCategoryId())
                .build();

        editProduct = Product.builder()
                .productId(1L)
                .name("Queso")
                .price(new BigDecimal("4500.00"))
                .description("Tipo Campesino")
                .minStock(12)
                .categoryId(lacteos.getCategoryId())
                .build();

        productDTO = new ProductDTO(1L, "Leche", "Lácteos", new BigDecimal("3500.00"), "https://res.cloudinary.com/fake/image.jpg", "H", 5L);
        productInventoryDTO = new ProductInventoryDTO(1L, "Leche", 10, 20L);
    }

    @Test
    @DisplayName("Debería guardar un producto subiendo la imagen a Cloudinary exitosamente")
    void saveProductSuccessfully() throws IOException {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);
        String fakeUrl = "https://res.cloudinary.com/fake/image.jpg";
        when(cloudinaryService.uploadImage(image)).thenReturn(fakeUrl);
        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = service.saveProduct(productSetup, image);

        // Assert
        assertNotNull(result);
        assertEquals(savedProduct.getProductId(), result.getProductId());
        assertEquals(fakeUrl, result.getImageUrl());
        verify(cloudinaryService, times(1)).uploadImage(image);
        verify(repository, times(1)).save(productSetup);
    }

    @Test
    @DisplayName("Debería buscar y retornar un producto por su ID")
    void findProductByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(savedProduct);

        // Act
        Product result = service.findProductById(id);

        // Assert
        assertNotNull(result);
        assertEquals(savedProduct, result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debería listar todos los productos en formato DTO")
    void findAllProductsSuccessfully() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(productDTO));

        // Act
        List<ProductDTO> result = service.findAllProducts();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Leche", result.getFirst().name());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería actualizar los campos de un producto existente sin cambiar la imagen")
    void updateProductSuccessfully() throws IOException {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(savedProduct);
        when(repository.save(any(Product.class))).thenReturn(editProduct);

        // Act
        Product result = service.updateProduct(id, editProduct, null);

        // Assert
        assertNotNull(result);
        assertEquals("Queso", result.getName());
        assertEquals(new BigDecimal("4500.00"), result.getPrice());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(savedProduct);
        verify(cloudinaryService, never()).uploadImage(any());
    }

    @Test
    @DisplayName("Debería actualizar el producto y subir la nueva imagen a Cloudinary")
    void updateProductWithImageSuccessfully() throws IOException {
        // Arrange
        Long id = 1L;
        MultipartFile image = mock(MultipartFile.class);
        String newUrl = "https://res.cloudinary.com/fake/new-image.jpg";
        when(image.isEmpty()).thenReturn(false);
        when(repository.findById(id)).thenReturn(savedProduct);
        when(cloudinaryService.uploadImage(image)).thenReturn(newUrl);
        when(repository.save(any(Product.class))).thenReturn(editProduct);

        // Act
        Product result = service.updateProduct(id, editProduct, image);

        // Assert
        assertNotNull(result);
        verify(cloudinaryService, times(1)).uploadImage(image);
        verify(repository, times(1)).save(savedProduct);
    }

    @Test
    @DisplayName("Debería eliminar un producto por su ID")
    void deleteProductByIdSuccessfully() {
        // Arrange
        Long id = 1L;

        // Act
        service.deleteProductById(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debería contar la cantidad total de productos")
    void countProductsSuccessfully() {
        // Arrange
        when(repository.countProducts()).thenReturn(15L);

        // Act
        Long result = service.countProducts();

        // Assert
        assertEquals(15L, result);
        verify(repository, times(1)).countProducts();
    }

    @Test
    @DisplayName("Debería contar los productos con stock crítico")
    void productsWithCriticalStockSuccessfully() {
        // Arrange
        when(repository.countProductsWithCriticalStock()).thenReturn(3L);

        // Act
        Long result = service.productsWithCriticalStock();

        // Assert
        assertEquals(3L, result);
        verify(repository, times(1)).countProductsWithCriticalStock();
    }

    @Test
    @DisplayName("Debería retornar la lista general del inventario")
    void findGeneralInventorySuccessfully() {
        // Arrange
        when(repository.findGeneralInventory()).thenReturn(List.of(productInventoryDTO));

        // Act
        List<ProductInventoryDTO> result = service.findGeneralInventory();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findGeneralInventory();
    }

    @Test
    @DisplayName("Debería buscar y retornar productos filtrados por categoría")
    void findByCategorySuccessfully() {
        // Arrange
        Long categoryId = 1L;
        when(repository.findByCategory(categoryId)).thenReturn(List.of(productDTO));

        // Act
        List<ProductDTO> result = service.findByCategory(categoryId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findByCategory(categoryId);
    }

    @Test
    @DisplayName("Debería retornar el ProductDTO del producto correspondiente al ID dado")
    void findProductDTOByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        when(repository.findProductDTOById(id)).thenReturn(productDTO);

        // Act
        ProductDTO result = service.findProductDTOById(id);

        // Assert
        assertNotNull(result);
        assertEquals(productDTO, result);
        verify(repository, times(1)).findProductDTOById(id);
    }
}