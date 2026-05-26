package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SpringSaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByUser_UserId(Long userId);

    Long countByUser_UserId(Long userId);

    @Query("SELECT SUM(s.totalPrice) FROM SaleEntity s WHERE s.user.userId = :userId")
    BigDecimal getTotalSpentByUser(@Param("userId") Long userId);

    @Query("SELECT new com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO(" +
            "s.saleId, s.saleDate, p.name, l.lotId, d.quantity, d.unitPrice, (d.quantity * d.unitPrice)) " +
            "FROM SaleEntity s " +
            "JOIN s.details d " +
            "JOIN d.lot l " +
            "JOIN l.product p " +
            "WHERE s.user.userId = :userId " +
            "ORDER BY s.saleDate DESC")
    List<SaleHistoryDTO> getRecentPurchasesByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO(" +
            "s.saleId, s.saleDate, p.name, l.lotId, d.quantity, d.unitPrice, (d.quantity * d.unitPrice)) " +
            "FROM SaleEntity s " +
            "JOIN s.details d " +
            "JOIN d.lot l " +
            "JOIN l.product p " +
            "ORDER BY s.saleDate DESC")
    List<SaleHistoryDTO> getSaleHistory();
}
