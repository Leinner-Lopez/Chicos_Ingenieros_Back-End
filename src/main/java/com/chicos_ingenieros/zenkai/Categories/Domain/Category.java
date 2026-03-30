package com.chicos_ingenieros.zenkai.Categories.Domain;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long category_id;
    private String name;
    private String description;
    private List<Product> products;
}
