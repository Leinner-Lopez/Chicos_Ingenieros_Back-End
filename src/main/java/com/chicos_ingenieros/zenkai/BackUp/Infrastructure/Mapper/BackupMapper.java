package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;
import com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Entity.BackupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BackupMapper {

    Backup backupEntityToBackup(BackupEntity backupEntity);

    BackupEntity backupToBackupEntity(Backup backup);
}
