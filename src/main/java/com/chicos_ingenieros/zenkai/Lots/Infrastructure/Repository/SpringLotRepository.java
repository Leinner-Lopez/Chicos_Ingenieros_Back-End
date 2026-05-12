package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringLotRepository extends JpaRepository<LotEntity, Long> {
}
