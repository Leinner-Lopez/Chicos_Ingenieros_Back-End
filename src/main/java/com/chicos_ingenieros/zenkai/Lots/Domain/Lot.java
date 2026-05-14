package com.chicos_ingenieros.zenkai.Lots.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lot {
    private Long lotId;
    private LocalDate expirationDate;
    private Integer stockQuantity;
    private LotStatus status;
    private Long productId;
}
