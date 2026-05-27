package com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO;

public record ProductInventoryDTO(
        Long productId,
        String name,
        Integer minStock,
        Long totalStock
) {
}
