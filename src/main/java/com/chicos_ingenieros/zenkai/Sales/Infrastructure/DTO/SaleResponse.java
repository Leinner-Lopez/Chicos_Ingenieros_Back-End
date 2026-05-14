package com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponse(
        Long saleId,
        Long userId,
        LocalDateTime saleDate,
        BigDecimal totalPrice
) {
}
