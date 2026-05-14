package com.chicos_ingenieros.zenkai.Sales.Application.UseCases;

import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;

import java.util.List;

public interface SaleQueryUseCase {
    List<Sale> findSalesByUser(Long userId);
    List<SaleResponse> findAllSales();
}
