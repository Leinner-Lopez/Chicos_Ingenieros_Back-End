package com.chicos_ingenieros.zenkai.BackUp.Application.UseCases;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;

public interface BackupCreateUseCase {
    Backup createBackup(String createdBy);
}
