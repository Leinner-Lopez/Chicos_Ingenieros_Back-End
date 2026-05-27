package com.chicos_ingenieros.zenkai.Sales.Application.UseCases;

import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SaleQueryUseCase {
    List<Sale> findSalesByUser(Long userId);
    List<SaleResponse> findAllSales();
    Long countSalesByUser(Long userId);
    BigDecimal getTotalSpentByUser(Long userId);
    List<SaleHistoryDTO> getRecentPurchasesByUser(Long userId);
}
