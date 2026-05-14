package com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Entity.SaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringSaleDetailRepository extends JpaRepository<SaleDetailEntity, Long> {
    List<SaleDetailEntity> findBySale_SaleId(Long saleId);
    void deleteBySale_SaleId(Long saleId);
}
