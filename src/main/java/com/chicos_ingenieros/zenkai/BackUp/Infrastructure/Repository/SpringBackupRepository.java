package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Entity.BackupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringBackupRepository extends JpaRepository<BackupEntity, Long> {

    List<BackupEntity> findAllByOrderByCreatedAtDesc();
}
