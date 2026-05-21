package com.chicos_ingenieros.zenkai.Sales.Application.UseCases;

import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;

import java.util.List;

public interface SaleHistoryUseCase {
    List<SaleHistoryDTO> getSaleHistory();
}
