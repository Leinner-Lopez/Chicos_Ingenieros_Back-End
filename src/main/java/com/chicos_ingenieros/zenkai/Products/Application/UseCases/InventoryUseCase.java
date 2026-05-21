package com.chicos_ingenieros.zenkai.Products.Application.UseCases;

import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;

import java.util.List;

public interface InventoryUseCase {
    List<ProductInventoryDTO> findGeneralInventory();
}
