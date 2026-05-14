package com.chicos_ingenieros.zenkai.Lots.Application;

import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotCountUseCase;
import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotCrudUseCase;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LotService implements LotCrudUseCase, LotCountUseCase {

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
    public List<Lot> findAllLots() {
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

}
