package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SpringLotRepository extends JpaRepository<LotEntity, Long> {

    @Query("SELECT l FROM LotEntity l WHERE l.product.productId = :productId " +
            "AND l.status = 'AVAILABLE' AND l.stockQuantity > 0 " +
            "ORDER BY l.expirationDate ASC")
    List<LotEntity> findAvailableLotsOrderedByExpiration(@Param("productId") Long productId);

    @Query("SELECT l FROM LotEntity l " +
            "WHERE l.expirationDate BETWEEN :fechaInicio AND :fechaFin " +
            "AND l.status = :status " +
            "ORDER BY l.expirationDate ASC")
    List<LotEntity> findLotsExpireThisWeek(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("status") LotStatus status
    );

    @Query("SELECT COALESCE(SUM(l.stockQuantity * p.price), 0) " +
            "FROM LotEntity l JOIN l.product p " +
            "WHERE l.status = :statusVencido " +
            "AND l.expirationDate BETWEEN :inicioMes AND :finMes")
    BigDecimal calculateCostOfShrinksOfTheMonth(
            @Param("statusVencido") LotStatus statusVencido,
            @Param("inicioMes") LocalDate inicioMes,
            @Param("finMes") LocalDate finMes
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LotEntity l SET l.status = 'EXPIRED' " +
            "WHERE l.expirationDate < :today AND l.status NOT IN ('EXPIRED', 'EMPTY')")
    int markAsExpired(@Param("today") LocalDate today);
}
