package com.chicos_ingenieros.zenkai.BackUp.Application.UseCases;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;

public interface BackupRestoreUseCase {
    /** Replaces the data with the backup content and returns the safety backup taken beforehand. */
    Backup restoreBackup(Long id, String adminEmail);
}
