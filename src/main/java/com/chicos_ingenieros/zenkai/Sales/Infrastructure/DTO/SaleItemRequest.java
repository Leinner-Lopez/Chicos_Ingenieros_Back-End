package com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO;

public record SaleItemRequest(Long productId, Integer quantity, Long userId) {
}
