package com.chicos_ingenieros.zenkai.Sales.Application.UseCases;

import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleItemRequest;

public interface SaleCrudUseCase {
    Sale createSale(SaleItemRequest request);
    Sale findSaleById(Long saleId);
    void cancelSale(Long saleId);
}
