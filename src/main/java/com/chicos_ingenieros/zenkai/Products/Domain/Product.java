package com.chicos_ingenieros.zenkai.Products.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer minStock;
    private Long categoryId;
    private String categoryName;
}
