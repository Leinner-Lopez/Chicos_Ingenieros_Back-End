package com.chicos_ingenieros.zenkai.Lots.Application.UseCases;

import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;

import java.util.List;

public interface LotFeFoUseCase {
    List<Lot> findAvailableLotsByProductOrderedByExpiration(Long productId);
}
