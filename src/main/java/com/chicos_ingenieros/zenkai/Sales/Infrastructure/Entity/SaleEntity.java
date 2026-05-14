package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity;

import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Entity.SaleDetailEntity;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sale")
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime saleDate;

    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleDetailEntity> details;

}
