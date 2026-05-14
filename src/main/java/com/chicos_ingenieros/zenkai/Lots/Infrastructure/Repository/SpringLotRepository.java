package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringLotRepository extends JpaRepository<LotEntity, Long> {

    @Query("SELECT l FROM LotEntity l WHERE l.product.productId = :productId " +
            "AND l.status = 'AVAILABLE' AND l.stockQuantity > 0 " +
            "ORDER BY l.expirationDate ASC")
    List<LotEntity> findAvailableLotsOrderedByExpiration(@Param("productId") Long productId);
}
