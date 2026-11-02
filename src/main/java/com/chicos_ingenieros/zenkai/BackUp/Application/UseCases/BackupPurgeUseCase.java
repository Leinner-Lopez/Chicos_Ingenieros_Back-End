package com.chicos_ingenieros.zenkai.BackUp.Application.UseCases;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;

public interface BackupPurgeUseCase {
    /** Empties the database and returns the safety backup taken beforehand. */
    Backup purgeDatabase(String adminEmail);
}
