package com.chicos_ingenieros.zenkai.Lots.Domain;

import com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO.LotDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LotRepository {

    Lot save(Lot lot);

    Lot findById(Long id);

    List<LotDTO> findAll();

    void deleteById(Long id);

    Long countLots();

    List<Lot> findAvailableLotsByProductOrderedByExpiration(Long productId);

    List<LotDTO> findLotsExpireThisWeek();

    BigDecimal calculateCostOfShrinksOfTheMonth();

    void updateLotStatuses(LocalDate today);
}
