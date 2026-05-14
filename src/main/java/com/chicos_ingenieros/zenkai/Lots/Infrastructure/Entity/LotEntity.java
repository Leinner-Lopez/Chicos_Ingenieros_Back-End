package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity;


import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lot")
public class LotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    private LocalDate expirationDate;
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private LotStatus status;

    @JoinColumn(name="product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
