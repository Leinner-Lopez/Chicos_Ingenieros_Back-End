package com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO;
import java.time.LocalDate;

public record LotDTO(Long lotId, LocalDate expirationDate, Integer stockQuantity, String productName) {
}
