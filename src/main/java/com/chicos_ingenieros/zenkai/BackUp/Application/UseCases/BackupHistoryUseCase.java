package com.chicos_ingenieros.zenkai.BackUp.Application.UseCases;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;

import java.io.InputStream;
import java.util.List;

public interface BackupHistoryUseCase {
    List<Backup> findAllBackups();

    Backup findBackupById(Long id);

    InputStream openBackupFile(Long id);
}
