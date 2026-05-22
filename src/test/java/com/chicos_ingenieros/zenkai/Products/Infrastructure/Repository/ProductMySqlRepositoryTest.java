package com.chicos_ingenieros.zenkai.Products.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void save_ShouldReturnSavedProduct() {
        Product domainProduct = new Product();
        ProductEntity initialEntity = new ProductEntity();
        ProductEntity savedEntity = new ProductEntity();
        Product expectedProduct = new Product();

        when(mapper.ProducttoProductEntity(domainProduct)).thenReturn(initialEntity);
        when(repository.save(initialEntity)).thenReturn(savedEntity);
        when(mapper.ProductEntitytoProduct(savedEntity)).thenReturn(expectedProduct);
        Product actualProduct = productMySqlRepository.save(domainProduct);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        verify(repository, times(1)).save(initialEntity);
    }

    @Test
    void findById_ShouldReturnProduct_WhenIdExists() {
        Long id = 1L;
        ProductEntity productEntity = new ProductEntity();
        Product expectedProduct = new Product();

        when(repository.findById(id)).thenReturn(Optional.of(productEntity));
        when(mapper.ProductEntitytoProduct(productEntity)).thenReturn(expectedProduct);

        Product actualProduct = productMySqlRepository.findById(id);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productMySqlRepository.findById(id);
        });

        assertEquals("Product wit id 1 not found", exception.getMessage());
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_ShouldReturnListOfProducts() {
        ProductEntity entity1 = new ProductEntity();
        ProductEntity entity2 = new ProductEntity();
        Product domain1 = new Product();
        Product domain2 = new Product();

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));
        when(mapper.ProductEntitytoProduct(entity1)).thenReturn(domain1);
        when(mapper.ProductEntitytoProduct(entity2)).thenReturn(domain2);

        List<Product> actualProducts = productMySqlRepository.findAll();

        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(domain1, actualProducts.get(0));
        assertEquals(domain2, actualProducts.get(1));
    }

    @Test
    void deleteById_ShouldInvokeRepositoryDelete() {
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        productMySqlRepository.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void countProducts_ShouldReturnTotalCount() {
        Long expectedCount = 42L;
        when(repository.count()).thenReturn(expectedCount);

        Long actualCount = productMySqlRepository.countProducts();

        assertEquals(expectedCount, actualCount);
        verify(repository, times(1)).count();
    }

    @Test
    void countProductsWithCriticalStock_ShouldReturnCriticalCountWithAvailableStatus() {
        Long expectedCriticalCount = 5L;
        when(repository.countProductWithCriticalStock(LotStatus.AVAILABLE)).thenReturn(expectedCriticalCount);

        Long actualCriticalCount = productMySqlRepository.countProductsWithCriticalStock();

        assertEquals(expectedCriticalCount, actualCriticalCount);
        verify(repository, times(1)).countProductWithCriticalStock(LotStatus.AVAILABLE);
    }

    @Test
    void findGeneralInventory_ShouldReturnListOfProductInventoryDTO() {
        ProductInventoryDTO dto1 = mock(ProductInventoryDTO.class);
        ProductInventoryDTO dto2 = mock(ProductInventoryDTO.class);
        List<ProductInventoryDTO> expectedInventory = List.of(dto1, dto2);

        when(repository.findGeneralInventory(LotStatus.AVAILABLE)).thenReturn(expectedInventory);

        List<ProductInventoryDTO> actualInventory = productMySqlRepository.findGeneralInventory();

        assertNotNull(actualInventory);
        assertEquals(2, actualInventory.size());
        assertEquals(expectedInventory, actualInventory);
        verify(repository, times(1)).findGeneralInventory(LotStatus.AVAILABLE);
    }
}