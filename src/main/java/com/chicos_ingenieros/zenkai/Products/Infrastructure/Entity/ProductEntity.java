package com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity;

import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

    private Integer min_stock;

    @JoinColumn(name="category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<LotEntity> lots;
}
