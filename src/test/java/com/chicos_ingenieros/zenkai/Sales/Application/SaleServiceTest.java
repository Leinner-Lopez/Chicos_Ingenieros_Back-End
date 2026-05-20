package com.chicos_ingenieros.zenkai.Sales.Application;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetailRepository;
import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Domain.SaleRepository;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleItemRequest;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Mapper.SaleMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock private SaleRepository saleRepository;
    @Mock
    private SaleDetailRepository saleDetailRepository;
    @Mock private LotRepository lotRepository;
    @Mock private SaleMapper saleMapper;

    @InjectMocks
    private SaleService service;

    // ─── Fixtures ────────────────────────────────────────────────

    Lot lotA = Lot.builder()
            .lotId(1L).productId(10L)
            .stockQuantity(30)
            .expirationDate(LocalDate.of(2025, 6, 1))
            .productPrice(new BigDecimal("3500.00"))
            .status(LotStatus.AVAILABLE).build();

    Lot lotB = Lot.builder()
            .lotId(2L).productId(10L)
            .stockQuantity(20)
            .expirationDate(LocalDate.of(2025, 12, 31))
            .productPrice(new BigDecimal("3500.00"))
            .status(LotStatus.AVAILABLE).build();

    Sale savedSale = Sale.builder()
            .saleId(100L)
            .userId(5L)
            .totalPrice(new BigDecimal("175000.00"))
            .build();

    SaleDetail savedDetail = SaleDetail.builder()
            .detailId(1L).saleId(100L)
            .lotId(1L).quantity(50)
            .unitPrice(new BigDecimal("3500.00")).build();

    // ─── createSale ──────────────────────────────────────────────

    @Test
    void createSale_singleLot_success() {
        // Compra de 10 unidades, lotA tiene 30 → solo se usa lotA
        SaleItemRequest request = new SaleItemRequest(10L, 10, 5L);

        when(lotRepository.findAvailableLotsByProductOrderedByExpiration(10L))
                .thenReturn(List.of(lotA));
        when(lotRepository.save(any(Lot.class))).thenReturn(lotA);
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);
        when(saleDetailRepository.save(any(SaleDetail.class))).thenReturn(savedDetail);

        Sale result = service.createSale(request);

        assertNotNull(result);
        assertEquals(100L, result.getSaleId());
        // lotA descontó 10 unidades → queda 20
        assertEquals(20, lotA.getStockQuantity());
        verify(lotRepository, times(1)).save(lotA);
        verify(saleRepository, times(1)).save(any(Sale.class));
        verify(saleDetailRepository, times(1)).save(any(SaleDetail.class));
    }

    @Test
    void createSale_multiLot_FEFO_consumesBothLots() {
        // Compra de 40 unidades: lotA(30) se agota, lotB(20) aporta los 10 restantes
        SaleItemRequest request = new SaleItemRequest(10L, 40, 5L);

        when(lotRepository.findAvailableLotsByProductOrderedByExpiration(10L))
                .thenReturn(List.of(lotA, lotB));
        when(lotRepository.save(any(Lot.class))).thenAnswer(inv -> inv.getArgument(0));
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);
        when(saleDetailRepository.save(any(SaleDetail.class))).thenReturn(savedDetail);

        Sale result = service.createSale(request);

        assertNotNull(result);
        assertEquals(0, lotA.getStockQuantity());
        assertEquals(LotStatus.EMPTY, lotA.getStatus());  // lotA se vació
        assertEquals(10, lotB.getStockQuantity());         // lotB descontó 10
        verify(lotRepository, times(2)).save(any(Lot.class)); // se guardaron ambos lotes
        verify(saleDetailRepository, times(2)).save(any(SaleDetail.class));
    }

    @Test
    void createSale_noLots_throwsResourceNotFoundException() {
        SaleItemRequest request = new SaleItemRequest(10L, 5, 5L);

        when(lotRepository.findAvailableLotsByProductOrderedByExpiration(10L))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.createSale(request));
        verify(saleRepository, never()).save(any());
    }

    @Test
    void createSale_insufficientStock_throwsIlegalActionException() {
        // Solo hay 30 unidades en total pero se piden 50
        SaleItemRequest request = new SaleItemRequest(10L, 50, 5L);

        when(lotRepository.findAvailableLotsByProductOrderedByExpiration(10L))
                .thenReturn(List.of(lotA)); // lotA solo tiene 30
        when(lotRepository.save(any(Lot.class))).thenReturn(lotA);

        assertThrows(IlegalActionException.class, () -> service.createSale(request));
        verify(saleRepository, never()).save(any());
    }

    // ─── findSaleById ────────────────────────────────────────────

    @Test
    void findSaleById_success() {
        when(saleRepository.findById(100L)).thenReturn(Optional.of(savedSale));

        Sale result = service.findSaleById(100L);

        assertEquals(savedSale, result);
    }

    @Test
    void findSaleById_notFound_throwsException() {
        when(saleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findSaleById(99L));
    }

    // ─── findSalesByUser ─────────────────────────────────────────

    @Test
    void findSalesByUser() {
        when(saleRepository.findByUserId(5L)).thenReturn(List.of(savedSale));

        List<Sale> result = service.findSalesByUser(5L);

        assertEquals(1, result.size());
        assertEquals(savedSale, result.getFirst());
    }

    // ─── findAllSales ────────────────────────────────────────────

    @Test
    void findAllSales() {
        SaleResponse response = mock(SaleResponse.class);
        when(saleRepository.findAll()).thenReturn(List.of(savedSale));
        when(saleMapper.toResponse(savedSale)).thenReturn(response);

        List<SaleResponse> result = service.findAllSales();

        assertEquals(1, result.size());
        verify(saleMapper, times(1)).toResponse(savedSale);
    }

    // ─── cancelSale ──────────────────────────────────────────────

    @Test
    void cancelSale_success() {
        when(saleRepository.findById(100L)).thenReturn(Optional.of(savedSale));
        when(saleDetailRepository.findBySaleId(100L)).thenReturn(List.of(savedDetail));
        when(lotRepository.findById(1L)).thenReturn(lotA);

        int stockAntes = lotA.getStockQuantity(); // 20 (ya descontado por el fixture anterior)
        service.cancelSale(100L);

        assertEquals(stockAntes + savedDetail.getQuantity(), lotA.getStockQuantity());
        assertEquals(LotStatus.AVAILABLE, lotA.getStatus());
        verify(lotRepository, times(1)).save(lotA);
        verify(saleDetailRepository, times(1)).deleteBySaleId(100L);
        verify(saleRepository, times(1)).deleteById(100L);
    }

    @Test
    void cancelSale_notFound_throwsException() {
        when(saleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.cancelSale(99L));
        verify(saleDetailRepository, never()).deleteBySaleId(any());
        verify(saleRepository, never()).deleteById(any());
    }
}