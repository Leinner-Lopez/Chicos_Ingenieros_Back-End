package com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO;

import java.math.BigDecimal;

public record ProductDTO(Long product_id, String name , String categoryName,
                         BigDecimal price, String description) {
}
