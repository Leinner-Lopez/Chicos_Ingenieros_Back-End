package com.chicos_ingenieros.zenkai.Lots.Application;

import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotServiceTest {

    @Mock
    private LotRepository repository;

    @InjectMocks
    private LotService service;

    Lot newLot = Lot.builder()
            .lotId(null)
            .productId(1L)
            .stockQuantity(50)
            .expirationDate(LocalDate.of(2025, 12, 31))
            .status(LotStatus.AVAILABLE)
            .build();

    Lot savedLot = Lot.builder()
            .lotId(1L)
            .productId(1L)
            .stockQuantity(50)
            .expirationDate(LocalDate.of(2025, 12, 31))
            .status(LotStatus.AVAILABLE)
            .build();

    Lot editLot = Lot.builder()
            .lotId(1L)
            .productId(2L)
            .stockQuantity(30)
            .expirationDate(LocalDate.of(2026, 6, 15))
            .status(LotStatus.AVAILABLE)
            .build();

    // ─── saveLot ─────────────────────────────────────────────────

    @Test
    void saveLot() {
        when(repository.save(newLot)).thenReturn(savedLot);

        Lot result = service.saveLot(newLot);

        assertEquals(savedLot, result);
        verify(repository, times(1)).save(newLot);
    }

    // ─── findLotById ─────────────────────────────────────────────

    @Test
    void findLotById_success() {
        when(repository.findById(1L)).thenReturn(savedLot);

        Lot result = service.findLotById(1L);

        assertEquals(savedLot, result);
    }

    @Test
    void findLotById_notFound_returnsNull() {
        when(repository.findById(99L)).thenReturn(null);

        Lot result = service.findLotById(99L);

        assertNull(result);
    }

    // ─── findAllLots ─────────────────────────────────────────────

    @Test
    void findAllLots() {
        when(repository.findAll()).thenReturn(List.of(savedLot));

        List<Lot> result = service.findAllLots();

        assertEquals(1, result.size());
        assertEquals(savedLot, result.getFirst());
    }

    // ─── updateLot ───────────────────────────────────────────────

    @Test
    void updateLot_success() {
        when(repository.findById(1L)).thenReturn(savedLot);
        when(repository.save(savedLot)).thenReturn(editLot);

        Lot result = service.updateLot(1L, editLot);

        assertEquals(editLot, result);
        verify(repository, times(1)).save(savedLot);
    }

    @Test
    void updateLot_notFound_returnsNull() {
        when(repository.findById(99L)).thenReturn(null);

        Lot result = service.updateLot(99L, editLot);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    // ─── deleteLotById ───────────────────────────────────────────

    @Test
    void deleteLotById() {
        service.deleteLotById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    // ─── countLots ───────────────────────────────────────────────

    @Test
    void countLots() {
        when(repository.countLots()).thenReturn(3L);

        Long result = service.countLots();

        assertEquals(3L, result);
    }

    // ─── findAvailableLotsByProductOrderedByExpiration ───────────

    @Test
    void findAvailableLotsByProductOrderedByExpiration_withResults() {
        Lot olderLot = Lot.builder()
                .lotId(2L).productId(1L)
                .stockQuantity(20)
                .expirationDate(LocalDate.of(2025, 6, 1))
                .status(LotStatus.AVAILABLE).build();

        when(repository.findAvailableLotsByProductOrderedByExpiration(1L))
                .thenReturn(List.of(olderLot, savedLot));

        List<Lot> result = service.findAvailableLotsByProductOrderedByExpiration(1L);

        assertEquals(2, result.size());
        assertEquals(olderLot, result.getFirst()); // FEFO: vence antes → primero
    }

    @Test
    void findAvailableLotsByProductOrderedByExpiration_noResults() {
        when(repository.findAvailableLotsByProductOrderedByExpiration(99L))
                .thenReturn(List.of());

        List<Lot> result = service.findAvailableLotsByProductOrderedByExpiration(99L);

        assertTrue(result.isEmpty());
    }
}