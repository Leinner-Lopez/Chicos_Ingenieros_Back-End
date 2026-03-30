package com.chicos_ingenieros.zenkai.Products.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Products.Infrastructure.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringProductRepository extends JpaRepository<ProductEntity, Long> {
}
