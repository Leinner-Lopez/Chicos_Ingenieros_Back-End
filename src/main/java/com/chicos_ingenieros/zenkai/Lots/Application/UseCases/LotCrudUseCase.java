package com.chicos_ingenieros.zenkai.Lots.Application.UseCases;

import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;

import java.util.List;

public interface LotCrudUseCase {
    Lot saveLot(Lot lot);
    Lot findLotById(Long id);
    List<Lot> findAllLots();
    Lot updateLot(Long id, Lot lot);
    void deleteLotById(Long id);
}
