package com.chicos_ingenieros.zenkai.BackUp.Domain;

import java.util.List;

public interface BackupRepository {

    Backup save(Backup backup);

    Backup findById(Long id);

    List<Backup> findAll();
}
