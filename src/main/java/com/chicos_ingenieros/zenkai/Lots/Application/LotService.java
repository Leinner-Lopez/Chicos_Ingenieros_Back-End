package com.chicos_ingenieros.zenkai.Lots.Application;

import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.DashboardAdminUseCase;
import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotCountUseCase;
import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotCrudUseCase;
import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotFeFoUseCase;
import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotStatusUpdateUseCase;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO.LotDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LotService implements LotCrudUseCase, LotCountUseCase, LotFeFoUseCase, DashboardAdminUseCase, LotStatusUpdateUseCase {

    private final LotRepository repository;

    @Override
    public Lot saveLot(Lot lot) {
        return repository.save(lot);
    }

    @Override
    public Lot findLotById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<LotDTO> findAllLots() {
        return repository.findAll();
    }

    @Override
    public Lot updateLot(Long id, Lot lot) {
        Lot lotDB = repository.findById(id);
        if (lotDB != null) {
            lotDB.setExpirationDate(lot.getExpirationDate());
            lotDB.setStatus(lot.getStatus());
            lotDB.setProductId(lot.getProductId());
            lotDB.setStockQuantity(lot.getStockQuantity());
            return repository.save(lotDB);
        }
        return null;
    }

    @Override
    public void deleteLotById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countLots() {
        return repository.countLots();
    }

    @Override
    public List<Lot> findAvailableLotsByProductOrderedByExpiration(Long productId) {
        return repository.findAvailableLotsByProductOrderedByExpiration(productId);
    }

    @Override
    public List<LotDTO> findLotsExpireThisWeek() {
        return repository.findLotsExpireThisWeek();
    }

    @Override
    public BigDecimal calculateCostOfShrinksOfTheMonth() {
        return repository.calculateCostOfShrinksOfTheMonth();
    }

    @Override
    public void updateLotStatuses() {
        repository.updateLotStatuses(LocalDate.now());
    }
}
