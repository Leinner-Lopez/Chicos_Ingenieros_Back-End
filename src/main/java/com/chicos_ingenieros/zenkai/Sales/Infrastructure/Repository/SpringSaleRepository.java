package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringSaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByUser_UserId(Long userId);
}
