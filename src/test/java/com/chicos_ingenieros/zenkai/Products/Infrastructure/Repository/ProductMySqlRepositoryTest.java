package com.chicos_ingenieros.zenkai.Products.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductMySqlRepositoryTest {

    @Mock
    private SpringProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductMySqlRepository productMySqlRepository;

    private Product productDomain;
    private ProductEntity productEntity;
    private ProductDTO productDTO;
    private ProductInventoryDTO productInventoryDTO;

    @BeforeEach
    void setUp() {
        productDomain = Product.builder()
                .productId(1L)
                .name("Arroz")
                .price(new BigDecimal("2500.00"))
                .build();

        productEntity = new ProductEntity();
        productEntity.setProductId(1L);
        productEntity.setName("Arroz");

        productDTO = new ProductDTO(
                1L, "Arroz", "Granos", new BigDecimal("2500.00"), "url", "H", 10L
        );

        productInventoryDTO = new ProductInventoryDTO(1L, "Arroz", 10, 150L);
    }

    @Test
    @DisplayName("Debería mapear y guardar un producto correctamente devolviendo el dominio")
    void saveShouldReturnSavedProduct() {
        // Arrange
        when(mapper.ProducttoProductEntity(productDomain)).thenReturn(productEntity);
        when(repository.save(productEntity)).thenReturn(productEntity);
        when(mapper.ProductEntitytoProduct(productEntity)).thenReturn(productDomain);

        // Act
        Product actualProduct = productMySqlRepository.save(productDomain);

        // Assert
        assertNotNull(actualProduct);
        assertEquals(productDomain, actualProduct);
        verify(mapper, times(1)).ProducttoProductEntity(productDomain);
        verify(repository, times(1)).save(productEntity);
        verify(mapper, times(1)).ProductEntitytoProduct(productEntity);
    }

    @Test
    @DisplayName("Debería retornar un producto de dominio cuando el ID existe en la base de datos")
    void findByIdShouldReturnProductWhenIdExists() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(productEntity));
        when(mapper.ProductEntitytoProduct(productEntity)).thenReturn(productDomain);

        // Act
        Product actualProduct = productMySqlRepository.findById(id);

        // Assert
        assertNotNull(actualProduct);
        assertEquals(productDomain, actualProduct);
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).ProductEntitytoProduct(productEntity);
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException si el ID del producto no existe")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productMySqlRepository.findById(id)
        );

        assertEquals("Product wit id 1 not found", exception.getMessage());
        verify(repository, times(1)).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Debería retornar la lista de catálogos mapeada a DTO directamente desde el SpringRepository")
    void findAllShouldReturnListOfProductDTO() {
        // Arrange & Corrección: Tu código llama a findCatalogProductsWithStock()
        when(repository.findCatalogProductsWithStock()).thenReturn(List.of(productDTO));

        // Act
        List<ProductDTO> actualProducts = productMySqlRepository.findAll();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals(productDTO, actualProducts.getFirst());
        verify(repository, times(1)).findCatalogProductsWithStock();
        verifyNoInteractions(mapper); // No se usa el mapper manual en este método
    }

    @Test
    @DisplayName("Debería invocar la eliminación por ID en el SpringRepository")
    void deleteByIdShouldInvokeRepositoryDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        // Act
        productMySqlRepository.deleteById(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debería retornar el conteo total de filas del SpringRepository")
    void countProductsShouldReturnTotalCount() {
        // Arrange
        Long expectedCount = 42L;
        when(repository.count()).thenReturn(expectedCount);

        // Act
        Long actualCount = productMySqlRepository.countProducts();

        // Assert
        assertEquals(expectedCount, actualCount);
        verify(repository, times(1)).count();
    }

    @Test
    @DisplayName("Debería contar productos con stock crítico pasando explícitamente el estado AVAILABLE")
    void countProductsWithCriticalStockShouldReturnCountWithAvailableStatus() {
        // Arrange
        Long expectedCriticalCount = 5L;
        when(repository.countProductWithCriticalStock(LotStatus.AVAILABLE)).thenReturn(expectedCriticalCount);

        // Act
        Long actualCriticalCount = productMySqlRepository.countProductsWithCriticalStock();

        // Assert
        assertEquals(expectedCriticalCount, actualCriticalCount);
        verify(repository, times(1)).countProductWithCriticalStock(LotStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Debería retornar el listado de inventario general filtrando por estado AVAILABLE")
    void findGeneralInventoryShouldReturnListOfProductInventoryDTO() {
        // Arrange
        List<ProductInventoryDTO> expectedInventory = List.of(productInventoryDTO);
        when(repository.findGeneralInventory(LotStatus.AVAILABLE)).thenReturn(expectedInventory);

        // Act
        List<ProductInventoryDTO> actualInventory = productMySqlRepository.findGeneralInventory();

        // Assert
        assertNotNull(actualInventory);
        assertEquals(1, actualInventory.size());
        assertEquals(expectedInventory, actualInventory);
        verify(repository, times(1)).findGeneralInventory(LotStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Debería retornar la lista de productos DTO filtrados por el ID de la categoría")
    void findByCategoryShouldReturnListOfProductDTO() {
        // Arrange
        Long categoryId = 2L;
        when(repository.findByCategory_CategoryId(categoryId)).thenReturn(List.of(productDTO));

        // Act
        List<ProductDTO> actualProducts = productMySqlRepository.findByCategory(categoryId);

        // Assert
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals(productDTO, actualProducts.getFirst());
        verify(repository, times(1)).findByCategory_CategoryId(categoryId);
    }
}