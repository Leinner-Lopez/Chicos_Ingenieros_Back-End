package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.DTO;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;

public record BackupOperationResponse(String message, Backup safetyBackup) {
}
