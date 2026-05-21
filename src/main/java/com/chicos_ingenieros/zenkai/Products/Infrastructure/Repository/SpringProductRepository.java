package com.chicos_ingenieros.zenkai.Products.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SpringProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("SELECT COUNT(p) FROM ProductEntity p " +
            "WHERE (SELECT COALESCE(SUM(l.stockQuantity), 0) " +
            "       FROM LotEntity l " +
            "       WHERE l.product = p AND l.status = :status) <= p.minStock")
    Long countProductWithCriticalStock(@Param("status") LotStatus status);

    @Query("SELECT new com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO(" +
            "p.productId, p.name, p.minStock, COALESCE(SUM(l.stockQuantity), 0L)) " +
            "FROM ProductEntity p " +
            "LEFT JOIN LotEntity l ON l.product = p AND l.status = :status " +
            "GROUP BY p.productId, p.name, p.minStock " +
            "ORDER BY p.name ASC")
    List<ProductInventoryDTO> findGeneralInventory(@Param("status") LotStatus status);
}
