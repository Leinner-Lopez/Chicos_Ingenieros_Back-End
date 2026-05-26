package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO.LotDTO;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Mapper.LotMapper;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotMySqlRepositoryTest {

    @Mock
    private SpringLotRepository repository;

    @Mock
    private LotMapper mapper;

    @InjectMocks
    private LotMySqlRepository lotMySqlRepository;

    @Test
    void save_ShouldReturnSavedLot() {
        Lot lot = new Lot();
        LotEntity lotEntity = new LotEntity();
        LotEntity savedEntity = new LotEntity();
        Lot expectedLot = new Lot();

        when(mapper.lotToLotEntity(lot)).thenReturn(lotEntity);
        when(repository.save(lotEntity)).thenReturn(savedEntity);
        when(mapper.lotEntityToLot(savedEntity)).thenReturn(expectedLot);

        Lot actualLot = lotMySqlRepository.save(lot);

        assertNotNull(actualLot);
        assertEquals(expectedLot, actualLot);
        verify(repository, times(1)).save(lotEntity);
    }

    @Test
    void findById_ShouldReturnLot_WhenIdExists() {
        Long id = 1L;
        LotEntity lotEntity = new LotEntity();
        Lot expectedLot = new Lot();

        when(repository.findById(id)).thenReturn(Optional.of(lotEntity));
        when(mapper.lotEntityToLot(lotEntity)).thenReturn(expectedLot);

        Lot actualLot = lotMySqlRepository.findById(id);

        assertNotNull(actualLot);
        assertEquals(expectedLot, actualLot);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            lotMySqlRepository.findById(id);
        });

        assertEquals("Lot with id 1 not found", exception.getMessage());
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_ShouldReturnListOfLotDTOs() {
        CategoryEntity savedCategory = CategoryEntity.builder()
                .categoryId(1L)
                .name("Lácteos")
                .description("Productos derivados de la leche").build();
        ProductEntity productEntity = new ProductEntity(1L, "Queso", new BigDecimal("3500.00"),"H",6,"URL",savedCategory);
        ProductEntity productEntity2 = new ProductEntity(2L, "Leche", new BigDecimal("3500.00"),"H",6,"URL",savedCategory);
        LotEntity entity1 = new LotEntity(1L, LocalDate.now(), 3, LotStatus.AVAILABLE,productEntity );
        LotEntity entity2 = new LotEntity(1L, LocalDate.now(), 3, LotStatus.AVAILABLE,productEntity2);
        LotDTO dto1 = new LotDTO(1L, LocalDate.now(),3, "Queso");
        LotDTO dto2 = new LotDTO(2L, LocalDate.now(),6, "Leche");

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));
        when(mapper.lotEntityToLotDTO(entity1)).thenReturn(dto1);
        when(mapper.lotEntityToLotDTO(entity2)).thenReturn(dto2);

        List<LotDTO> actualDTOs = lotMySqlRepository.findAll();

        assertNotNull(actualDTOs);
        assertEquals(2, actualDTOs.size());
        assertEquals(dto1, actualDTOs.get(0));
        assertEquals(dto2, actualDTOs.get(1));
    }

    @Test
    void deleteById_ShouldInvokeRepositoryDelete() {
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        lotMySqlRepository.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void countLots_ShouldReturnTotalCount() {
        Long expectedCount = 10L;
        when(repository.count()).thenReturn(expectedCount);

        Long actualCount = lotMySqlRepository.countLots();

        assertEquals(expectedCount, actualCount);
        verify(repository, times(1)).count();
    }

    @Test
    void findAvailableLotsByProductOrderedByExpiration_ShouldReturnOrderedLots() {
        Long productId = 5L;
        LotEntity entity1 = new LotEntity();
        Lot lot1 = new Lot();

        when(repository.findAvailableLotsOrderedByExpiration(productId)).thenReturn(List.of(entity1));
        when(mapper.lotEntityToLot(entity1)).thenReturn(lot1);

        List<Lot> actualLots = lotMySqlRepository.findAvailableLotsByProductOrderedByExpiration(productId);

        assertNotNull(actualLots);
        assertEquals(1, actualLots.size());
        assertEquals(lot1, actualLots.getFirst());
    }

    @Test
    void findLotsExpireThisWeek_ShouldCalculateDatesAndReturnLotDTOs() {
        LocalDate hoy = LocalDate.now();
        LocalDate finDeSemana = hoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LotEntity entity = new LotEntity();
        LotDTO dto = new LotDTO(1L, LocalDate.now(),3, "Queso");

        when(repository.findLotsExpireThisWeek(hoy, finDeSemana, LotStatus.AVAILABLE))
                .thenReturn(List.of(entity));
        when(mapper.lotEntityToLotDTO(entity)).thenReturn(dto);

        List<LotDTO> actualDTOs = lotMySqlRepository.findLotsExpireThisWeek();

        assertNotNull(actualDTOs);
        assertEquals(1, actualDTOs.size());
        assertEquals(dto, actualDTOs.getFirst());
        verify(repository, times(1)).findLotsExpireThisWeek(hoy, finDeSemana, LotStatus.AVAILABLE);
    }

    @Test
    void calculateCostOfShrinksOfTheMonth_ShouldCalculateDatesAndReturnCost() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthFinish = today.with(TemporalAdjusters.lastDayOfMonth());
        BigDecimal expectedCost = new BigDecimal("1500.50");

        when(repository.calculateCostOfShrinksOfTheMonth(LotStatus.EXPIRED, monthStart, monthFinish))
                .thenReturn(expectedCost);

        BigDecimal actualCost = lotMySqlRepository.calculateCostOfShrinksOfTheMonth();

        assertNotNull(actualCost);
        assertEquals(expectedCost, actualCost);
        verify(repository, times(1)).calculateCostOfShrinksOfTheMonth(LotStatus.EXPIRED, monthStart, monthFinish);
    }

    @Test
    void updateLotStatuses_ShouldCallMarkAsExpiredWithGivenDate() {
        LocalDate today = LocalDate.now();
        when(repository.markAsExpired(today)).thenReturn(3);

        lotMySqlRepository.updateLotStatuses(today);

        verify(repository, times(1)).markAsExpired(today);
    }
}