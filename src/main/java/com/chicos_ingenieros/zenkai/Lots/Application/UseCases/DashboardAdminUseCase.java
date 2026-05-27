package com.chicos_ingenieros.zenkai.Lots.Application.UseCases;

import com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO.LotDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardAdminUseCase {
    List<LotDTO> findLotsExpireThisWeek();
    BigDecimal calculateCostOfShrinksOfTheMonth();
}
