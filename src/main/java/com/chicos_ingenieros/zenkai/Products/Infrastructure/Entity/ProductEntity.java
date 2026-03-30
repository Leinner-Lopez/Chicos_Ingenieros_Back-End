package com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity;

import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String description;

    private int min_stock;

    @JoinColumn(name="category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;
}
