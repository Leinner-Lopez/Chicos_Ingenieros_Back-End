package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Mapper.SaleMapper;
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
class SaleMySqlRepositoryTest {

    @Mock
    private SpringSaleRepository springRepository;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleMySqlRepository saleMySqlRepository;

    @Test
    void save_ShouldReturnSavedSale_WhenSuccessful() {
        Sale domainSale = new Sale();
        SaleEntity saleEntity = new SaleEntity();
        SaleEntity savedEntity = new SaleEntity();
        Sale expectedSale = new Sale();

        when(saleMapper.toEntity(domainSale)).thenReturn(saleEntity);
        when(springRepository.save(saleEntity)).thenReturn(savedEntity);
        when(saleMapper.toDomain(savedEntity)).thenReturn(expectedSale);

        Sale actualSale = saleMySqlRepository.save(domainSale);

        assertNotNull(actualSale);
        assertEquals(expectedSale, actualSale);
        verify(springRepository, times(1)).save(saleEntity);
    }

    @Test
    void findById_ShouldReturnOptionalWithSale_WhenIdExists() {
        Long saleId = 1L;
        SaleEntity saleEntity = new SaleEntity();
        Sale expectedSale = new Sale();

        when(springRepository.findById(saleId)).thenReturn(Optional.of(saleEntity));
        when(saleMapper.toDomain(saleEntity)).thenReturn(expectedSale);

        Optional<Sale> actualSaleOpt = saleMySqlRepository.findById(saleId);

        assertTrue(actualSaleOpt.isPresent());
        assertEquals(expectedSale, actualSaleOpt.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        Long saleId = 1L;
        when(springRepository.findById(saleId)).thenReturn(Optional.empty());

        Optional<Sale> actualSaleOpt = saleMySqlRepository.findById(saleId);

        assertTrue(actualSaleOpt.isEmpty());
        verifyNoInteractions(saleMapper); // Asegura que no se intente mapear nada
    }

    @Test
    void findByUserId_ShouldReturnListOfSales_WhenUserHasSales() {
        Long userId = 2L;
        List<SaleEntity> entityList = List.of(new SaleEntity(), new SaleEntity());
        List<Sale> expectedDomainList = List.of(new Sale(), new Sale());

        when(springRepository.findByUser_UserId(userId)).thenReturn(entityList);
        when(saleMapper.toDomainList(entityList)).thenReturn(expectedDomainList);

        List<Sale> actualSales = saleMySqlRepository.findByUserId(userId);

        assertNotNull(actualSales);
        assertEquals(2, actualSales.size());
        assertEquals(expectedDomainList, actualSales);
    }

    @Test
    void deleteById_ShouldInvokeRepositoryDelete() {
        Long saleId = 1L;
        doNothing().when(springRepository).deleteById(saleId);

        saleMySqlRepository.deleteById(saleId);

        verify(springRepository, times(1)).deleteById(saleId);
    }

    @Test
    void findAll_ShouldReturnListOfAllSales() {
        SaleEntity entity1 = new SaleEntity();
        SaleEntity entity2 = new SaleEntity();
        Sale sale1 = new Sale();
        Sale sale2 = new Sale();

        when(springRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(saleMapper.toDomain(entity1)).thenReturn(sale1);
        when(saleMapper.toDomain(entity2)).thenReturn(sale2);

        List<Sale> actualSales = saleMySqlRepository.findAll();

        assertNotNull(actualSales);
        assertEquals(2, actualSales.size());
        assertEquals(sale1, actualSales.get(0));
        assertEquals(sale2, actualSales.get(1));
    }

    @Test
    void getSaleHistory_ShouldReturnHistoryDtoList() {

        SaleHistoryDTO dto1 = mock(SaleHistoryDTO.class);
        SaleHistoryDTO dto2 = mock(SaleHistoryDTO.class);
        List<SaleHistoryDTO> expectedHistory = List.of(dto1, dto2);

        when(springRepository.getSaleHistory()).thenReturn(expectedHistory);

        List<SaleHistoryDTO> actualHistory = saleMySqlRepository.getSaleHistory();

        assertNotNull(actualHistory);
        assertEquals(2, actualHistory.size());
        assertEquals(expectedHistory, actualHistory);
        verify(springRepository, times(1)).getSaleHistory();
    }
}