package com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Entity;

import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
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
@Table(name = "sale_details")
public class SaleDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private SaleEntity sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotEntity lot;

    private Integer quantity;

    private BigDecimal unitPrice;
}
