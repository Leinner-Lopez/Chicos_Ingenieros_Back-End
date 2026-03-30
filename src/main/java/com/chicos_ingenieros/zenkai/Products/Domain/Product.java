package com.chicos_ingenieros.zenkai.Products.Domain;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
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
    private Long product_id;
    private String name;
    private BigDecimal price;
    private String description;
    private int min_stock;
    private Category category;
}
