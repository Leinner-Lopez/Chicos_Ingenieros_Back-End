package com.chicos_ingenieros.zenkai.Lots.Domain;

import java.util.List;

public interface LotRepository {

    Lot save(Lot lot);

    Lot findById(Long id);

    List<Lot> findAll();

    void deleteById(Long id);

    Long countLots();

    List<Lot> findAvailableLotsByProductOrderedByExpiration(Long productId);
}
