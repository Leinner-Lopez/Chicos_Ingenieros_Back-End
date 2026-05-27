package com.chicos_ingenieros.zenkai.Products.Application.UseCases;

import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;

import java.util.List;

public interface InventoryUseCase {
    List<ProductInventoryDTO> findGeneralInventory();
    List<ProductDTO> findByCategory(Long categoryId);
}
