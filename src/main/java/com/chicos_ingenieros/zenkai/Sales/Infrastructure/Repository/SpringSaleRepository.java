package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringSaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByUser_UserId(Long userId);

    @Query("SELECT new com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO(" +
            "s.saleId, s.saleDate, p.name, l.lotId, d.quantity, d.unitPrice, (d.quantity * d.unitPrice)) " +
            "FROM SaleEntity s " +
            "JOIN s.details d " +
            "JOIN d.lot l " +
            "JOIN l.product p " +
            "ORDER BY s.saleDate DESC")
    List<SaleHistoryDTO> getSaleHistory();
}
