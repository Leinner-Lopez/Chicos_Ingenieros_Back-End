package com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleHistoryDTO(
        Long saleId,
        LocalDateTime saleDate,
        String productName,
        Long lotId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
