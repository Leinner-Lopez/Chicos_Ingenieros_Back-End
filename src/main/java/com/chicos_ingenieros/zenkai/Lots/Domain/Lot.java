package com.chicos_ingenieros.zenkai.Lots.Domain;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
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
    private Long idLot;
    private LocalDate expirationDate;
    private Integer stockQuantity;
    private LotStatus status;
    private Product product;
}
